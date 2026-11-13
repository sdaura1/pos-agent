# Key Exchange — Implementation Notes

Status: S4a-1, S4a-2 and S4a-3 implemented
Related: [`13-key-exchange.md`](13-key-exchange.md), [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md)

This document explains what was actually built for key exchange and terminal
provisioning: the design, every component, the failure semantics, the tests, and
what is deliberately left unresolved until NIBSS supplies the key-exchange
payload and hash definition.

## 1. What this slice does

The app cannot transact until three things are true:

1. the Kozen terminal service is available and version-compatible,
2. the terminal holds the keys it needs (at minimum the TSK for DE128),
3. the EMV/host parameters are downloaded.

This slice builds the **contract, order and transport** for those steps. It does
not guess the vendor payloads. When a step cannot be completed, the app reports
a stable reason and refuses to start a card transaction.

Two principles drove the implementation:

- **Fail closed.** A missing key, missing hash or missing service must block a
  sale. It must never degrade into sending a message with a guessed field.
- **Separate what can be proven from what is ambiguous.** Only a request that
  provably never reached the host may be retried. Everything else becomes
  `UNKNOWN` and is reconciled.

## 2. Architecture

```
ui / use case
     |
     v
EnsureTerminalProvisionedUseCase
     |
     v
TerminalProvisioning (port)          SecretKeyStore (port)
     |                                     |
     +--> Kozen adapter (S4a-4)             +--> InMemorySecretKeyStore
     +--> Unprovisioned placeholder        +--> Android Keystore (later)
     +--> FakeTerminalProvisioning
     |
     v
ProvisioningStateMachine

Key exchange transport (S4a-2):
NibssConnection -> Iso8583Codec -> jpos packager -> MessageFraming
     |
     +--> TaggedFieldCodec (DE62/DE63)
     +--> MessageHasher (interface, fail-closed)
     +--> KeyCheckValue / HexCodec (key verification and encoding)
```

The provisioning subsystem never imports `com.kozen.*`. The vendor adapter that
performs the real downloads implements `TerminalProvisioning` and uses the
transport in `data/remote/nibss/iso`.

### Naming conventions

- The package supplies context; the type name supplies the role.
- `Nibss` prefixes boundary types; `Iso8583` names the protocol.
- State is the lifecycle (`ProvisioningState.Provisioned`); result is the
  answer to one call (`ProvisioningResult.Success`).
- Exceptions name the broken promise (`RequestNotSent`,
  `ResponseNotReceived`, `ResponseMismatch`).
- Acronyms are expanded in type names (`KeyCheckValue`, `TaggedField`).
- `CTMS` remains in prose as the name of the NIBSS system, not in class names.

## 3. S4a-1 — Provisioning state machine

### States

| State | Meaning |
| --- | --- |
| `Unprovisioned` | No attempt has run |
| `DownloadingKeys` | TMK/TPK/TSK/IPEK download in progress |
| `DownloadingParameters` | AID/CA-key/TAC/limits download in progress |
| `Provisioned` | Keys and parameters proven present |
| `Failed(reason, detail)` | Provisioning could not complete |

### Transitions

| From | To | Trigger |
| --- | --- | --- |
| `Unprovisioned`, `Failed`, `Provisioned` | `DownloadingKeys` | `beginKeyDownload()` |
| `DownloadingKeys` | `DownloadingParameters` | `beginParameterDownload()` |
| `DownloadingParameters` | `Provisioned` | `markProvisioned()` |
| any non-`Provisioned` state | `Failed` | `markFailed(reason, detail)` |
| `Failed` | `Unprovisioned` | `reset()` |

Illegal transitions throw `IllegalArgumentException` and leave the state
unchanged. In particular:

- `Provisioned` cannot be reached without a completed parameter download.
- A `Provisioned` terminal cannot be marked failed by a stray error; a service
  disconnect does not erase keys.
- `DownloadingParameters` cannot be entered before keys.

### Port

`TerminalProvisioning` exposes:

- `state: StateFlow<ProvisioningState>` for UI and gating,
- `ensureProvisioned(): ProvisioningResult` — idempotent and single-flight,
- `reprovision(): ProvisioningResult` — forces a new attempt for rotation or after
  failure.

### Readiness gate

`EnsureTerminalProvisionedUseCase` is the only thing the purchase flow should call. It
maps failures to `Response.Failure` with a stable code:

| Failure | Domain code | Domain error type |
| --- | --- | --- |
| `SERVICE_UNAVAILABLE` | `TERMINAL_SERVICE_UNAVAILABLE` | `Unknown` |
| `VERSION_MISMATCH` | `TERMINAL_VERSION_MISMATCH` | `Conflict` |
| `PERMISSION_DENIED` | `TERMINAL_PERMISSION_DENIED` | `Conflict` |
| `KEY_DOWNLOAD_FAILED` | `TERMINAL_KEY_DOWNLOAD_FAILED` | `Network` |
| `PARAMETER_DOWNLOAD_FAILED` | `TERMINAL_PARAMETER_DOWNLOAD_FAILED` | `Network` |
| `HASH_NOT_CONFIGURED` | `TERMINAL_HASH_NOT_CONFIGURED` | `Conflict` |
| `UNSUPPORTED_DEVICE` | `TERMINAL_UNSUPPORTED_DEVICE` | `Unknown` |
| `UNKNOWN` | `TERMINAL_PROVISIONING_UNKNOWN` | `Unknown` |

The UI resolves the code to a string resource; the domain carries no user-facing
text.

### Production behaviour

`UnavailableTerminalProvisioning` is bound in Hilt until the Kozen adapter
exists. It reports `SERVICE_UNAVAILABLE` on every call. This is intentional: a
build without a key-exchange adapter can never start a card transaction.

`FakeTerminalProvisioning` defaults to `Provisioned`, is deterministic, serialises
`ensureProvisioned()` behind a mutex, and exposes an attempt counter for tests.
`reprovision()` always performs a new attempt.

## 4. S4a-2 — Transport and codec

### Framing

`MessageFraming` implements the CTMS wire rule: a 2-byte big-endian length followed
by the ASCII ISO 8583 payload.

- Maximum frame is 65,535 bytes.
- `read` returns `null` on a clean EOF, throws on a truncated header or payload,
  and rejects zero-length frames.
- Reads are stream-safe: a socket returning one byte at a time still produces
  the correct frame.

### Packager

The repository already contained `app/src/main/resources/jpos.xml`. It was
adopted as the **single** CTMS dialect packager instead of adding a second,
divergent file.

Three corrections were required against specification v1.0.2:

| Field | Before | After | Why |
| --- | --- | --- | --- |
| DE52 PIN block | `IFA_BINARY`, 8 bytes | `IF_CHAR`, 16 chars | CTMS carries ASCII hex, not raw bytes |
| DE53 security info | `IFA_BINARY`, 48 bytes | `IF_CHAR`, 96 chars | Same reason |
| DE120 geo coordinates | missing | `IFA_LLLCHAR`, 999 | Mandatory on every request |

0800/0810 were added to the packager documentation. A secondary-bitmap test
proves DE120 and DE128 pack and unpack correctly.

### Codec

`Iso8583Codec` wraps the packager:

- `newMessage(mti)` sets MTI and packager,
- `pack` calls `ISOMsg.pack()` so jpos recalculates the bitmap,
- `unpack` creates an `ISOMsg` with the shared packager,
- `write`/`read` apply framing.

A golden test pins the 0800 layout. For a message with DE3, DE7, DE11, DE12,
DE13, DE41, DE62 and DE64, the bitmap is `2238000000800005` and the payload is
byte-for-byte stable. This is the reference for future builders.

### Tagged fields

`TaggedFieldCodec` encodes and decodes the `tag(2) + length(3) + value` shape used by
DE62 and DE63. It validates tags, lengths and bounds, supports empty values and
provides a map view. It is the building block for timeout tags, serial numbers,
key serial numbers and parameter downloads.

### Channel

`NibssConnection` is a single-flight socket connection:

- one request in flight, correlated by STAN,
- connect on demand, reconnect on the next call after a failure,
- 2-byte framing through `Iso8583Codec`,
- `Dispatchers.IO` for all blocking work,
- configurable connect/read timeouts and retry/backoff.

### Failure classification

`NibssExchangeException` is deliberately split:

| Type | Meaning | Caller action |
| --- | --- | --- |
| `NotSent` | Connection failed, or the write failed | Safe to retry with the same reference |
| `Unknown` | The write completed but no usable response arrived | Reverse/reconcile; never re-authorise |
| `Protocol` | Response malformed, STAN mismatch, or packing failure | Treat as a terminal/host error |

`sendAndReceiveWithRetry` retries only `NotSent`, with exponential backoff bounded by
configuration. Tests prove both halves: a refused connection is retried, while a
connection that closes after the request is **not** resent.

## 5. S4a-3 — Key material, KCV and hashing

### Key aliases and store

`SecretKeyAlias` names the material the system may need:

`TERMINAL_MASTER_KEY`, `TERMINAL_PIN_KEY`, `TERMINAL_SESSION_KEY`,
`IPEK_TRACK2`, `IPEK_EMV`.

`SecretKeyStore` is the port. Its contract:

- copy on write and copy on read,
- zero the stored bytes on removal,
- never log, back up or serialise keys in clear.

`InMemorySecretKeyStore` is the current implementation. It is process-local:
a restart requires a fresh sign-on and key download. That is acceptable for the
current decision state and safer than persisting keys without an agreed at-rest
scheme. The store is mutex-protected. Caller-owned arrays and returned arrays are
isolated from the stored copy.

### KCV

`KeyCheckValue` computes the Key Check Value: the first three bytes of the key encrypting
an eight-byte zero block, rendered as six uppercase hex characters.

- 8-byte key → single DES,
- 16-byte key → two-key 3DES expanded to K1‖K2‖K1,
- 24-byte key → three-key 3DES,
- any other length is rejected.

Known-answer vectors used in tests:

| Key | KCV |
| --- | --- |
| `0123456789ABCDEF` | `D5D44F` |
| `0123456789ABCDEFFEDCBA9876543210` | `08D7B4` |
| `0123456789ABCDEFFEDCBA98765432100123456789ABCDEF` | `08D7B4` |

`KeyCheckValue.matches` compares case-insensitively in constant time. KCVs are how a
download is verified without exposing the key.

### Hash

`MessageHasher` is a one-method interface:

```kotlin
fun hash(message: ByteArray, sessionKey: ByteArray): String
```

The result is uppercase hex formatted for DE64 or DE128.

Two implementations ship:

- `UnconfiguredMessageHasher` — throws `MessageHashNotConfiguredException`.
  This is bound in Hilt. Sending a guessed hash is worse than refusing to send.
- `FakeMessageHasher` — SHA-256 over `sessionKey || message`, for fixtures
  and tests only. It is explicitly **not** the NIBSS composition.

The exact composition (field order, padding, encoding, whether the length header
or bitmap is included) remains an open question. When NIBSS supplies a
known-answer vector, only the bound implementation changes.

### Hex and SHA-256 utilities

`HexCodec` provides uppercase encoding, tolerant decoding and constant-time
comparison. `Sha256` provides the digest. They are used by the key check value,
the hash and tests. The SHA-256 primitive is verified against the NIST vectors
for `abc` and the empty string.

## 6. File inventory

| File | Purpose |
| --- | --- |
| `domain/terminal/model/ProvisioningState.kt` | State model |
| `domain/terminal/model/ProvisioningFailureReason.kt` | Stable failure reasons |
| `domain/terminal/model/ProvisioningResult.kt` | Result of an attempt |
| `domain/terminal/model/ProvisioningStateMachine.kt` | Transition validation |
| `domain/terminal/model/ProvisioningErrorCode.kt` | Stable UI-facing codes |
| `domain/terminal/model/ProvisioningFailureMapper.kt` | Reason → `DomainError` mapping |
| `domain/terminal/model/SecretKeyAlias.kt` | Key names |
| `domain/terminal/port/TerminalProvisioning.kt` | Provisioning port |
| `domain/terminal/port/SecretKeyStore.kt` | Key storage port |
| `domain/terminal/usecase/EnsureTerminalProvisionedUseCase.kt` | Readiness gate |
| `data/device/provisioning/UnavailableTerminalProvisioning.kt` | Fail-closed default |
| `data/device/fake/FakeTerminalProvisioning.kt` | Deterministic fake |
| `data/device/keystore/InMemorySecretKeyStore.kt` | Process-local key store |
| `data/remote/nibss/iso/MessageFraming.kt` | Length framing |
| `data/remote/nibss/iso/Iso8583Packager.kt` | Loads `jpos.xml` |
| `data/remote/nibss/iso/Iso8583Codec.kt` | ISO pack/unpack and framing |
| `data/remote/nibss/iso/TaggedFieldCodec.kt` | DE62/DE63 TLV codec |
| `data/remote/nibss/iso/NibssConnectionConfig.kt` | Connection settings |
| `data/remote/nibss/iso/NibssExchangeException.kt` | Failure classification |
| `data/remote/nibss/iso/NibssConnection.kt` | Socket send/receive and correlation |
| `data/remote/nibss/iso/HexCodec.kt` | Hex encoding, decoding, constant-time compare |
| `data/remote/nibss/iso/Sha256.kt` | SHA-256 hex digest |
| `data/remote/nibss/iso/security/KeyCheckValue.kt` | Key check values |
| `data/remote/nibss/iso/hash/MessageHasher.kt` | Hash port and fail-closed default |
| `data/remote/nibss/iso/hash/FakeMessageHasher.kt` | Test hash |
| `di/ProvisioningModule.kt` | Binds the provisioning placeholder |
| `di/TerminalSecurityModule.kt` | Binds the key store and unconfigured hasher |

Modified: `app/src/main/resources/jpos.xml` (single dialect packager, DE52/DE53
ASCII hex, DE120 added, 0800/0810 documented).

## 7. Tests

The key-exchange suite is 83 tests; the whole project is 108.

| Suite | Tests | Proves |
| --- | --- | --- |
| `ProvisioningStateMachineTest` | 10 | Order, illegal transitions, failure, reset, refresh |
| `EnsureTerminalProvisionedUseCaseTest` | 6 | Success, every failure code, detail propagation |
| `FakeTerminalProvisioningTest` | 7 | Idempotency, refresh, retry, single-flight |
| `InMemorySecretKeyStoreTest` | 9 | Copy isolation, overwrite, aliases, removal, empty key |
| `KeyCheckValueTest` | 7 | DES/3DES vectors, lengths, case-insensitive match |
| `HexCodecTest` | 4 | Encoding, decoding, constant-time compare |
| `Sha256Test` | 2 | NIST vectors for `abc` and empty input |
| `MessageHasherTest` | 4 | Fail-closed default, fake determinism and format |
| `MessageFramingTest` | 9 | Length prefix, trickle reads, truncation, limits |
| `TaggedFieldCodecTest` | 13 | Round trip, empty values, malformed input |
| `Iso8583CodecTest` | 6 | Golden 0800, field round trip, secondary bitmap, ASCII hex |
| `NibssConnectionTest` | 6 | Loopback exchange, STAN correlation, retry only when not sent |

Run them with:

```
./gradlew --offline :app:testDebugUnitTest
```

## 8. How the pieces are used

### Gate a purchase

```kotlin
when (val provisioning = ensureTerminalProvisioned()) {
    is Response.Success -> startCardFlow()
    is Response.Failure -> showTerminalNotProvisioned(provisioning.error.code)
}
```

Observe `TerminalProvisioning.state` to enable or disable the start action and to
display progress while keys are downloaded.

### Verify a downloaded key

```kotlin
if (!KeyCheckValue.matches(expectedKcvFromHost, KeyCheckValue.calculate(loadedKey))) {
    // Reject the download and report KEY_DOWNLOAD_FAILED.
}
```

### Send an 0800

```kotlin
val request = codec.newMessage("0800").apply {
    set(3, "9B0000")
    set(7, transmissionDateTime)
    set(11, stan)
    set(12, localTime)
    set(13, localDate)
    set(41, terminalId)
    set(62, TaggedFieldCodec.encode(TaggedField("01", serialNumber)))
}

val response = connection.sendAndReceiveWithRetry(request)
```

A `NotSent` failure may be retried; `Unknown` must be reconciled.

### Hash a message

Once the composition is known, bind the certified `MessageHasher` and call
`hash(payload, tsk)`. Until then, the bound implementation throws
`MessageHashNotConfiguredException`, which maps to
`TERMINAL_HASH_NOT_CONFIGURED`.

## 9. Security properties and limitations

Properties:

- No key material is logged, persisted in clear or included in receipts.
- Key reads and writes are copied; removing a key zeroes the stored bytes.
- KCV comparison is constant-time and case-insensitive.
- The app fails closed if keys or the hash are not configured.
- Only provably unsent requests are retried.
- STAN correlation rejects responses for another request.

Limitations:

- `InMemorySecretKeyStore` is not durable and not hardware-backed. The Android
  Keystore-backed implementation is pending the storage decision.
- The hash composition is not implemented; only the interface and a fail-closed
  default exist.
- The semantic 0800 payloads for key download are not built; they wait on the
  NIBSS trace.
- jpos instantiates field packagers by class name, so R8 keep rules are required
  before release minification.
- The packager now covers the fields needed by 0200/0210/0420/0430 and 0800/0810,
  but no transaction builder exists yet.

## 10. Next steps

1. Obtain the NIBSS key-exchange trace and the DE128 known-answer vector.
2. Implement the semantic `9A`/`9B`/`9G`/`9I`/`9J` payload builders and the
   provisioning coordinator that drives the state machine.
3. Add the Android Keystore-backed `SecretKeyStore` once the storage decision is
   made.
4. Implement the certified `MessageHasher` and verify it against the vector.
5. Wire the real adapter into `ProvisioningModule` and remove the placeholder.
6. Replay the provisioning sequence on a Kozen terminal.

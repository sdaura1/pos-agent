# NIBSS CTMS Interface — Specification Digest

Source: `NUS SPECIFICATION v1.0.2.md` (converted from the September 2025 PDF)
Scope: POS/Payment Channel ↔ Central Terminal Management System (CTMS), part 1
Part 2 (Nigeria Central Switch, issuer/acquirer interface) is out of scope for this app.

## 1. Transport and framing

- TCP/IP, persistent connection.
- Each application message is prefixed by a **2-byte binary length header** giving the
  length of the application data that follows.
- No TPDU or header is specified beyond that length prefix; confirm with NIBSS.
- Bitmap: **ASCII ISO bitmap**. The document says "16- or 32-byte representation of the
  bitmap in HexCodec". In practice this means 16 or 32 hex characters (8 or 16 bytes): the
  primary bitmap, optionally followed by the secondary bitmap. Confirm the exact wire
  length, because every message here carries secondary fields (DE120, DE128).
- Encodings are ASCII/hex, not binary, except the length header. Amounts are `n12` in
  minor units.

## 2. Message types

| Class | MTI | Meaning |
| --- | --- | --- |
| Authorization | 0100 / 0101 / 0110 | Request / repeat / response (pre-auth, balance, PIN change) |
| Authorization advice | 0120 / 0121 / 0130 | Advice / repeat / response |
| Financial | 0200 / 0201 / 0210 | Request / repeat / response (purchase, refund) |
| Financial advice | 0220 / 0221 / 0230 | Advice / repeat / response |
| Reversal | 0420 / 0421 / 0430 | Advice / repeat / response |
| Network management | 0800 / 0810 | Request / response (sign-on, keys, parameters, call home) |

The app uses:

- **0200/0210** for purchase and refund (processing code distinguishes them).
- **0420/0421/0430** for reversals and timeouts.
- **0100/0110** for balance inquiry and pre-authorisation if in scope.
- **0800/0810** for key download, parameter download, call home and reporting.

## 3. Field presence matrix

Legend: M = mandatory, C = conditional, O = optional, blank = not in layout.

| DE | 0100 | 0110 | 0200 | 0210 | 0420/0421 | 0430 | 0800 | 0810 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 2 | M | M | M | M | M | M | | |
| 3 | M | M | M | M | M | M | M | |
| 4 | M | M | M | M | M | M | | |
| 5 | | | | | | | | |
| 7 | M | C | M | C | M | C | M | M |
| 9 | | | | | | | | |
| 11 | M | C | M | C | M | C | M | M |
| 12 | M | M | M | M | M | M | M | M |
| 13 | M | C | M | C | M | C | M | M |
| 14 | M | C | M | C | M | C | | |
| 15 | | C | M | C | M | C | | |
| 16 | | | M | M | M | | | |
| 18 | M | M | M | M | M | M | | |
| 22 | M | M | M | C | M | M | | |
| 23 | C | C | C | C | C | O | | |
| 25 | M | M | M | M | M | M | | |
| 26 | C | | C | C | C | | | |
| 28 | M | C | M | C | C | C | | |
| 29 | | | C | | | | | |
| 30 | | C | O | | C | C | | |
| 31 | | | | | | C | | |
| 32 | M | M | M | M | M | M | | |
| 33 | | C | | C | | C | | |
| 35 | C | C | C | C | C | C | | |
| 37 | M | M | M | M | M | M | | |
| 38 | | C | | *(missing)* | | C | | |
| 39 | | M | | *(missing)* | | M | | M |
| 40 | O | C | O | C | O | C | | |
| 41 | M | O | M | O | M | O | M | M |
| 42 | M | C | M | C | M | C | | |
| 43 | M | C | M | C | M | C | | |
| 49 | M | M | M | M | M | M | | |
| 52 | C | | C | | C | | | |
| 53 | C | | C | | | C | | C |
| 54 | C | C | C | C | C | C | | |
| 55 | C | C | C | C | | | | |
| 56 | O | | O | O | M | | | |
| 59 | O | C | O | C | O | C | | |
| 60 | C | C | C | C | C | | | |
| 62 | C | C | C | C | C | C | C | C |
| 63 | | | | | | | C | C |
| 64 | | | | | | | C | C |
| 90 | | | | | M | M | | |
| 95 | | | | | M | M | | |
| 102 | O | O | O | O | | O | | |
| 103 | O | O | O | O | | O | | |
| 120 | M | | M | | M | | | |
| 123 | M | M | M | M | M | M | | |
| 124 | C | C | C | C | C | C | | |
| 128 | M | M | M | M | M | M | | |

**Specification defect:** the 0210 layout omits DE38 (authorization code) and DE39
(response code), while 0110 and 0430 include them. A financial response without a
response code cannot be processed. Treat DE38/DE39 as present on 0210 and confirm with
NIBSS before coding the parser.

## 4. Field dictionary

| DE | Name | Format | Notes |
| --- | --- | --- | --- |
| 2 | Primary account number | `LLVAR n..19` | PAN |
| 3 | Processing code | `an6` | see section 5 |
| 4 | Amount, transaction | `n12` | minor units, transaction currency |
| 5 | Amount, settlement | `n12` | |
| 7 | Transmission date/time | `n10` | `MMDDhhmmss`, UTC |
| 9 | Conversion rate, settlement | `n8` | leftmost digit = decimal places |
| 11 | STAN | `n6` | unchanged across a transaction |
| 12 | Local time | `n6` | `hhmmss` |
| 13 | Local date | `n4` | `MMDD` |
| 14 | Expiry date | `n4` | `YYMM` |
| 15 | Settlement date | `n4` | `MMDD` |
| 16 | Conversion date | `n4` | `MMDD` |
| 18 | Merchant type | `n4` | MCC |
| 22 | POS entry mode | `N3` | 2 digits entry + 1 digit PIN capability |
| 23 | Card sequence number | `n3` | |
| 25 | POS condition code | `n2` | `00` normal presentment |
| 26 | POS PIN capture code | `n2` | `04`–`12`; `12` when unknown |
| 28 | Amount, transaction fee | `x+n8` | `C` or `D` prefix |
| 29/30/31 | Settlement / processing fees | `x+n8` | |
| 32 | Acquiring institution ID | `LLVAR an..11` | |
| 33 | Forwarding institution ID | `LLVAR n..11` | |
| 35 | Track 2 | `LLVAR z..37` | encrypted track-2 equivalent |
| 37 | Retrieval reference number | `an12` | |
| 38 | Authorization code | `n6` | response |
| 39 | Response code | `an2` | see section 7 |
| 40 | Service restriction code | `N3` | |
| 41 | Terminal ID | `ans8` | TID |
| 42 | Card acceptor ID | `ans15` | MID |
| 43 | Card acceptor name/location | `ans40` | 23 name + 13 city + 2 state + 2 country |
| 44 | Additional response data | `LLVAR ans..25` | |
| 49 | Currency, transaction | `n3` | NGN = `566` |
| 50 | Currency, settlement | `n3` | |
| 52 | PIN data | `Hex16` | 64-bit encrypted PIN block |
| 53 | Security control info | `Hex96` | key change / PIN change |
| 54 | Additional amounts | `LLLVAR an..120` | up to 6 amounts |
| 55 | ICC data | `LLLVAR ANS..510` | TLV, see section 8 |
| 56 | Message reason code | `LLLVAR n..4` | see section 9 |
| 58 | Authorizing agent ID | `LLLVAR n..11` | |
| 59 | Echo data | `LLLVAR ans..255` | must be echoed in the response |
| 60 | Payment information | `LLLVAR ans..999` | tagged bills/token data |
| 62 | Private field 1 | `LLLVAR ans..999` | tagged, see section 10 |
| 63 | Private field 2 | `LLLLVAR ans..9999` | parameter/key download payload |
| 64 | Primary message hash | `AN64` | SHA-256 hex |
| 67 | Extended payment code | `n2` | |
| 90 | Original data elements | `n42` | see section 11 |
| 95 | Replacement amounts | `n42` | see section 11 |
| 98 | Payee | `ans25` | |
| 100 | Receiving institution ID | `LLVAR n..11` | |
| 102/103 | Account identification 1/2 | `LLVAR n..28` | |
| 120 | POS geo coordinates | `LLLCHAR ans..999` | see section 12 |
| 123 | POS data code | `LLLVAR an15` | see section 13 |
| 124 | NFC data | `LLLLVAR ans..9999` | as received from the terminal |
| 128 | Secondary message hash | `AN64` | SHA-256 hex |

## 5. Processing codes (DE3)

| Code | Transaction | MTI |
| --- | --- | --- |
| 00 | Purchase | 0200 |
| 01 | Cash advance | 0200 |
| 09 | Purchase with cash back | 0200 |
| 20 | Refund / return | 0200 |
| 21 | Deposit | 0200 |
| 30 | Link account inquiry | 0100 |
| 31 | Balance inquiry | 0100 |
| 38 | Mini statement | 0100 |
| 40 | Fund transfer | 0200 |
| 48 | Bill payment | 0200 |
| 4A | Prepaid | 0200 |
| 4F | Purchase with additional data | 0200 |
| 60 | POS pre-authorisation | 0100 |
| 61 | POS pre-authorisation completion | 0220 |
| 90 | PIN change | 0100 |
| 9A | Terminal master key | 0800 |
| 9B | Terminal session key | 0800 |
| 9C | Terminal parameter download | 0800 |
| 9D | Call home | 0800 |
| 9E | CA public key download | 0800 |
| 9F | EMV AID download | 0800 |
| 9G | Terminal PIN key | 0800 |
| 9H | Daily transaction report download | 0800 |
| 9I | IPEK download, track 2 | 0800 |
| 9J | IPEK download, EMV | 0800 |
| 9K | Dynamic currency conversion | 0800 |

Account type, positions 3–4 (debit) and 5–6 (credit): `00` default, `10` savings,
`20` current, `30` credit, `40` universal, `50` investment.

## 6. POS entry mode (DE22)

PAN entry (positions 1–2):

| Code | Meaning |
| --- | --- |
| 00 | Unknown |
| 01 | Manual / keypad |
| 02 | Magnetic stripe |
| 03 | Barcode |
| 04 | OCR |
| 05 | ICC (CVV may be checked) |
| 07 | Contactless ICC |
| 90 | Magnetic stripe read from track 2 |
| 91 | Contactless magnetic stripe |
| 95 | ICC (CVV may not be checked) |

PIN capability (position 3): `0` unknown, `1` terminal accepts PIN, `2` terminal cannot
accept PIN.

## 7. Response codes (DE39)

| Code | Meaning |
| --- | --- |
| 00 | Approved or completed successfully |
| 01 | Refer to card issuer |
| 02 | Refer to card issuer, special condition |
| 03 | Invalid merchant |
| 04 | Pick-up card |
| 05 | Do not honor |
| 06 | Error |
| 07 | Pick-up card, special condition |
| 08 | Honor with identification |
| 09 | Request in progress |
| 10 | Approved, partial |
| 11 | Approved, VIP |
| 12 | Invalid transaction |
| 13 | Invalid amount |
| 14 | Invalid card number |
| 15 | No such issuer |
| 16 | Approved, update track 3 |
| 17 | Customer cancellation |
| 18 | Customer dispute |
| 19 | Re-enter transaction |
| 20 | Invalid response |
| 21 | No action taken |
| 22 | Suspected malfunction |
| 23 | Unacceptable transaction fee |
| 24 | File update not supported |
| 25 | Unable to locate record |
| 26 | Duplicate record |
| 27 | File update edit error |
| 28 | File update file locked |
| 29 | File update failed |
| 30 | Format error |
| 31 | Bank not supported |
| 32 | Completed partially |
| 33 | Expired card, pick-up |
| 34 | Suspected fraud, pick-up |
| 35 | Contact acquirer, pick-up |
| 36 | Restricted card, pick-up |
| 37 | Call acquirer security, pick-up |
| 38 | PIN tries exceeded, pick-up |
| 39 | No credit account |
| 40 | Function not supported |
| 41 | Lost card |
| 42 | No universal account |
| 43 | Stolen card |
| 44 | No investment account |
| 51 | Not sufficient funds |
| 52 | No check account |
| 53 | No savings account |
| 54 | Expired card |
| 55 | Incorrect PIN |
| 56 | No card record |
| 57 | Transaction not permitted to cardholder |
| 58 | Transaction not permitted on terminal |
| 59 | Suspected fraud |
| 60 | Contact acquirer |
| 61 | Exceeds withdrawal limit |
| 62 | Restricted card |
| 63 | Security violation |
| 64 | Original amount incorrect |
| 65 | Exceeds withdrawal frequency |
| 66 | Call acquirer security |
| 67 | Hard capture |
| 68 | Response received too late |
| 75 | PIN tries exceeded |
| 77 | Intervene, bank approval required |
| 78 | Intervene, bank approval required for partial amount |
| 90 | Cut-off in progress |
| 91 | Issuer or switch inoperative |
| 92 | Routing error |
| 93 | Violation of law |
| 94 | Duplicate transaction |
| 95 | Reconcile error |
| 96 | System malfunction |
| 98 | Exceeds cash limit |

Only `00` is an approval. Everything else is a decline or a referral. `68` and `95`
warrant reconciliation rather than a blind retry.

## 8. ICC data tags (DE55)

TLV: 2- or 4-hex-digit tag, 2-hex-digit length in bytes, then data.

Request tags required:

| Tag | Description | Max length | Presence |
| --- | --- | --- | --- |
| 9F26 | Application cryptogram | H8 | Y |
| 9F27 | Cryptogram information data | H1 | Y |
| 9F10 | Issuer application data | 32 | Y |
| 9F37 | Unpredictable number | H4 | Y |
| 9F36 | Application transaction counter | H2 | Y |
| 95 | Terminal verification results | H5 | Y |
| 9A | Transaction date | H3 | Y |
| 9C | Transaction type | H1 | Y |
| 9F02 | Transaction amount | H6 | Y |
| 5F2A | Transaction currency code | H2 | Y |
| 82 | Application interchange profile | H2 | Y |
| 9F1A | Terminal country code | H2 | Y |
| 9F34 | CVM results | H4 | N |
| 9F33 | Terminal capabilities | H3 | N |
| 9F35 | Terminal type | H1 | N |
| 9F1E | Interface device serial number | H8 | N |
| 84 | Dedicated file name | H16 | N |
| 9F09 | Application version number | H2 | N |
| 9F03 | Amount, other | H6 | N |
| 5F34 | PAN sequence number | H1 | N |
| 9F6E | Form factor indicator | H4 | N |

Response tags:

| Tag | Description | Max length |
| --- | --- | --- |
| 91 | Issuer authentication data | H32 |
| 71 | Issuer script 1 | H128 |
| 72 | Issuer script 2 | H128 |

Tags `91`, `71` and `72` must be fed back into the EMV kernel as part of the online
response (`setOnlineResponse` in the Kozen adapter).

## 9. Message reason codes (DE56)

| Code | Meaning |
| --- | --- |
| 1003 | Card issuer unavailable |
| 1006 | Under floor limit |
| 1376 | PIN verification failure |
| 1377 | Change dispensed |
| 1378 | IOU receipt printed |
| 1510 | Over floor limit |
| 1800 | Negative card |
| 4000 | Customer cancellation |
| 4001 | Unspecified, no action taken |
| 4004 | Completed partially |
| 4021 | Timeout waiting for response |

`0420` requires DE56. Use `4000` for a cashier cancellation and `4021` for a host
timeout.

## 10. Private field 62 tags

| Tag | Meaning | Notes |
| --- | --- | --- |
| 01 | POS serial number | request |
| 02 | CTMS date and time | response |
| 03 | Card acceptor identification code | |
| 04 | **Timeout** (seconds) | host tells the terminal how long to wait |
| 05 | Currency code | |
| 06 | Country code | |
| 07 | Call-home interval (hours) | |
| 08 | Merchant category code | |
| 09 | Application version | |
| 10 | Terminal model | |
| 11 | Call-home merchant information | |
| 12 | Communications service provider | |
| 52 | Merchant name and location | |

Tag 04 is operationally important: the host advertises the response deadline (the
document's example is 2 seconds). The client deadline should be derived from it, not
hardcoded.

## 11. Reversal fields

**DE90, original data elements (`n42`)**

| Position | Content |
| --- | --- |
| 1–4 | Original MTI |
| 5–10 | Original STAN |
| 11–20 | Original transmission date/time |
| 21–31 | Original acquirer institution ID (right-justified, zero-padded) |
| 32–42 | Original forwarding institution ID (right-justified, zero-padded) |

**DE95, replacement amounts (`n42`)**

| Position | Content |
| --- | --- |
| 1–12 | Actual amount, transaction |
| 13–24 | Actual amount, settlement |
| 25–33 | Actual transaction fee (`x+n8`) |
| 34–42 | Actual settlement fee (`x+n8`) |

A full reversal sets the replacement amounts to zero. A partial reversal carries the
corrected amount.

## 12. Geo coordinates (DE120)

Mandatory in every request and reversal. Tag-length-value string:

`01` + length (2 digits) + latitude, `02` + length (2 digits) + longitude.

Example: `010806.524400209003.37921` = latitude 6.52440, longitude 3.37921.

- Latitude `DD.ddddd` or `-DD.ddddd`, degrees 00–90.
- Longitude `DDD.ddddd` or `-DDD.ddddd`, degrees 000–180.
- Minimum 5 decimal places.

Implication: the app needs a location source on every transaction. On Kozen this can
come from the TerminalManager location module. A missing fix must have an explicit
policy because the field is mandatory.

## 13. POS data code (DE123)

Fifteen positions:

| Pos | Meaning | Example values |
| --- | --- | --- |
| 1 | Card data input capability | 5 magstripe+key+ICC, 9 ICC, A contactless ICC, B contactless magstripe |
| 2 | Cardholder authentication capability | 0 none, 1 PIN, 2 signature |
| 3 | Card capture capability | 0 none, 1 capture |
| 4 | Operating environment | 1 attended on-premise |
| 5 | Cardholder present | 0 present |
| 6 | Card present | 1 present |
| 7 | Actual card data input mode | 2 magstripe, 5 ICC, 7 contactless ICC, 8 contactless magstripe |
| 8 | Actual authentication method | 0 none, 1 PIN, 2 signature analysis, 5 manual |
| 9 | Authentication entity | 0 none, 2 terminal, 3 authorizing agent |
| 10 | Card data output capability | 0 unknown, 1 none, 3 ICC |
| 11 | Terminal output capability | 4 printing and display |
| 12 | PIN capture capability | 4–C for four to twelve characters |
| 13 | Terminal operator | 0 customer, 1 card acceptor |
| 14–15 | Terminal type | 01 POS |

## 14. Message hash and keys

- The hash is **SHA-256**, carried in DE64 when no secondary fields (DE65–DE128) are
  present, otherwise in DE128. Because DE120 is mandatory, requests always use DE128.
- The **Terminal Session Key (TSK)** seeds the hash algorithm.
- Key hierarchy:
  - Master/session: CTMK (clear, injected), TMK (encrypted under CTMK), TPK
    (encrypted under TMK), TSK (encrypted under TMK).
  - DUKPT: CTMK, TMK, BDK (injected in components), TSK, IPEK for track 2, IPEK for
    EMV.
- TSK is used both to seed the message hash and, for DUKPT, to derive per-transaction
  keys.
- Key download is a terminal-management exchange over 0800 (processing codes 9A, 9B,
  9G, 9I, 9J), with the key serial number supplied in DE62 tag 57 for BDK requests.
- EMV configuration can also be downloaded: CA keys (9E, DE63 tags 32–40) and AIDs
  (9F, DE63 tags 13–31), including TAC, limits and target percentages.

The document does **not** specify the exact hash composition, padding, field ordering
or how TSK seeds SHA-256. That must be obtained from NIBSS before the adapter can be
certified.

## 15. Implications for this app

1. **Transport is ISO 8583 over TCP**, not REST. The plan's provisional "REST first"
   decision is superseded. `jpos` is already a dependency and should carry the codec.
2. The app must implement **terminal provisioning**: key download and EMV parameter
   download over 0800, or accept them from a local signed configuration if NIBSS
   permits.
3. **DE120 is mandatory**, so location capture is part of the transaction path, not an
   optional feature.
4. **DE123 must be assembled** from the capture (entry mode, CVM, PIN capability,
   terminal type), so the domain capture model needs those values.
5. **DE128 must be computed on every request** from the assembled message and the TSK.
   This is a hard dependency on key download; no transactions until keys exist.
6. **Reversals are advice messages** with DE90/DE95 and a reason code. A timeout is
   not a failure: send `0420` with `4021` and reconcile.
7. **Issuer scripts and authentication data** (DE55 tags 91/71/72) must be returned to
   the EMV kernel, or contactless/chip transactions may not complete correctly.
8. **Echo data (DE59)** must be returned unchanged; it is useful for correlation but is
   not a substitute for the reference.

## 16. Open questions for NIBSS

1. Exact bitmap wire length (16 vs 32 hex characters) and whether a TPDU is used.
2. DE38/DE39 presence on 0210.
3. Hash composition and TSK seeding algorithm; encoding of the DE128 digest.
4. DE55 field length semantics (hex characters vs bytes) and tag ordering.
5. Reversal rules: when to send 0420 vs 0421, repeat count, and how long to wait.
6. Whether advice messages (0120/0220) are used by POS or only by the switch.
7. Approval of the mandatory-DE120 policy when no GPS fix is available.
8. Which message carries terminal parameter/key download payloads in full, and whether
   DUKPT or master/session is mandated for this acquirer.
9. Test host, credentials, and certification scripts.

## 17. Source traceability

| Topic | Spec location |
| --- | --- |
| Protocol / framing | Introduction, Protocol |
| Message types | Supported Message Type |
| Layouts | Authorization/Financial/Reversal/Network sections |
| Field formats | Field Definition DE2–DE128 |
| Response codes | Field #39 |
| ICC tags | Field #55 |
| Reversal fields | Fields #90, #95 |
| Geo | Field #120 |
| POS data code | Field #123 |
| Hash | Fields #64, #128 |
| Keys | Key Management |

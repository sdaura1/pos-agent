package com.example.pos_agent.domain.card.model

/**
 * Reference to an encrypted PIN block. The domain never sees a raw PIN and
 * never derives or validates keys.
 *
 * [ksn] is present for DUKPT keys, where the host needs the key serial number
 * to derive the transaction key. [keyId] identifies the injected key when the
 * acquirer uses a key index instead of DUKPT.
 */
data class PinBlockRef(
    val encryptedPinBlock: String,
    val ksn: String? = null,
    val keyId: String? = null,
) {
    init {
        require(isHex(encryptedPinBlock)) {
            "PIN block must be an even-length hex string"
        }
        require(encryptedPinBlock.length == 16 || encryptedPinBlock.length == 32) {
            "PIN block must be 16 or 32 hex characters"
        }
        require(ksn == null || isHex(ksn)) {
            "KSN must be an even-length hex string"
        }
        require(keyId == null || keyId.isNotBlank()) {
            "Key id cannot be blank"
        }
    }

    override fun toString(): String = "PinBlockRef(***)"
}

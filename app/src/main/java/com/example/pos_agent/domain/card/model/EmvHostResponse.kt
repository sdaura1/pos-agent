package com.example.pos_agent.domain.card.model

/** Which issuer script slot the data belongs to in DE55. */
enum class IssuerScriptTag(val wireTag: String) {
    SCRIPT_71("71"),
    SCRIPT_72("72"),
}

/** Issuer script command to feed back to the EMV kernel. */
data class IssuerScript(
    val tag: IssuerScriptTag,
    val data: EmvData,
)

/** Issuer authentication data (DE55 tag 91) returned by the host. */
data class IssuerAuthenticationData(val data: EmvData) {
    override fun toString(): String = "IssuerAuthenticationData(***)"
}

/**
 * EMV data the host returns and the kernel must consume after an online
 * response. The kernel supports at most one authentication block and two
 * script slots.
 */
data class EmvHostResponse(
    val issuerAuthenticationData: IssuerAuthenticationData? = null,
    val issuerScripts: List<IssuerScript> = emptyList(),
) {
    init {
        require(issuerScripts.size <= 2) {
            "At most two issuer scripts can be returned (tags 71 and 72)"
        }
    }
}

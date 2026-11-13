package com.example.pos_agent.domain.terminal.model

/** Why terminal provisioning could not reach or stay in a usable state. */
enum class ProvisioningFailureReason {
    /** The vendor terminal service is missing or would not bind. */
    SERVICE_UNAVAILABLE,

    /** The installed service version does not match the client. */
    VERSION_MISMATCH,

    /** The app is missing a vendor permission required by the service. */
    PERMISSION_DENIED,

    /** Key download (TMK/TPK/TSK/IPEK) failed or was rejected by the host. */
    KEY_DOWNLOAD_FAILED,

    /** Terminal or EMV parameter download failed. */
    PARAMETER_DOWNLOAD_FAILED,

    /** The message hash cannot be computed because no session key is present. */
    HASH_NOT_CONFIGURED,

    /** The terminal model or firmware is not supported. */
    UNSUPPORTED_DEVICE,

    UNKNOWN,
}

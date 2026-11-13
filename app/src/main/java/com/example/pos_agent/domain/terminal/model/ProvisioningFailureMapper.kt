package com.example.pos_agent.domain.terminal.model

import com.example.pos_agent.core.error.DomainError

/**
 * Maps a provisioning failure reason to the domain error the UI receives.
 *
 * The stable code lives in [ProvisioningErrorCode]; this file owns the type
 * choice and the fallback message. Adapters never build `DomainError` values.
 */
internal fun ProvisioningFailureReason.toDomainError(detail: String?): DomainError {
    val message = detail ?: defaultMessage()
    return when (this) {
        ProvisioningFailureReason.SERVICE_UNAVAILABLE,
        ProvisioningFailureReason.UNSUPPORTED_DEVICE,
        ProvisioningFailureReason.UNKNOWN,
        -> DomainError.Unknown(message = message, code = errorCode())

        ProvisioningFailureReason.VERSION_MISMATCH,
        ProvisioningFailureReason.PERMISSION_DENIED,
        ProvisioningFailureReason.HASH_NOT_CONFIGURED,
        -> DomainError.Conflict(message = message, code = errorCode())

        ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
        ProvisioningFailureReason.PARAMETER_DOWNLOAD_FAILED,
        -> DomainError.Network(message = message, code = errorCode())
    }
}

private fun ProvisioningFailureReason.errorCode(): String = when (this) {
    ProvisioningFailureReason.SERVICE_UNAVAILABLE -> ProvisioningErrorCode.SERVICE_UNAVAILABLE
    ProvisioningFailureReason.VERSION_MISMATCH -> ProvisioningErrorCode.VERSION_MISMATCH
    ProvisioningFailureReason.PERMISSION_DENIED -> ProvisioningErrorCode.PERMISSION_DENIED
    ProvisioningFailureReason.KEY_DOWNLOAD_FAILED -> ProvisioningErrorCode.KEY_DOWNLOAD_FAILED
    ProvisioningFailureReason.PARAMETER_DOWNLOAD_FAILED -> ProvisioningErrorCode.PARAMETER_DOWNLOAD_FAILED
    ProvisioningFailureReason.HASH_NOT_CONFIGURED -> ProvisioningErrorCode.HASH_NOT_CONFIGURED
    ProvisioningFailureReason.UNSUPPORTED_DEVICE -> ProvisioningErrorCode.UNSUPPORTED_DEVICE
    ProvisioningFailureReason.UNKNOWN -> ProvisioningErrorCode.UNKNOWN
}

private fun ProvisioningFailureReason.defaultMessage(): String = when (this) {
    ProvisioningFailureReason.SERVICE_UNAVAILABLE -> "Terminal service is unavailable"
    ProvisioningFailureReason.VERSION_MISMATCH -> "Terminal service version is not supported"
    ProvisioningFailureReason.PERMISSION_DENIED -> "Terminal permission is missing"
    ProvisioningFailureReason.KEY_DOWNLOAD_FAILED -> "Terminal keys could not be downloaded"
    ProvisioningFailureReason.PARAMETER_DOWNLOAD_FAILED -> "Terminal parameters could not be downloaded"
    ProvisioningFailureReason.HASH_NOT_CONFIGURED -> "Message hash is not configured"
    ProvisioningFailureReason.UNSUPPORTED_DEVICE -> "This terminal is not supported"
    ProvisioningFailureReason.UNKNOWN -> "Terminal provisioning failed"
}

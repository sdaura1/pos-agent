package com.example.pos_agent.data.remote.nibss.iso

import java.io.IOException

/**
 * Transport failures split by what can be proven about the request.
 *
 * This distinction is what prevents a double charge: only [RequestNotSent] may
 * be retried with a new authorisation. [ResponseNotReceived] must be reversed
 * and reconciled.
 */
sealed class NibssExchangeException(
    message: String,
    cause: Throwable? = null,
) : IOException(message, cause) {

    /** The request was never written, or failed while being written. */
    class RequestNotSent(message: String, cause: Throwable? = null) :
        NibssExchangeException(message, cause)

    /** The request was written but no usable response arrived. */
    class ResponseNotReceived(message: String, cause: Throwable? = null) :
        NibssExchangeException(message, cause)

    /** A response arrived but is malformed or does not match the request. */
    class ResponseMismatch(message: String, cause: Throwable? = null) :
        NibssExchangeException(message, cause)
}

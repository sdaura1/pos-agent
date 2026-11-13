package com.example.pos_agent.data.remote.nibss.iso

import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOMsg
import kotlin.time.Duration.Companion.milliseconds

/**
 * Single-flight NIBSS CTMS socket connection.
 *
 * One request is in flight at a time; STAN is used to correlate the response.
 * The connection reconnects on demand and classifies failures so callers can
 * tell a safe retry from an ambiguous outcome.
 */
class NibssConnection(
    private val config: NibssConnectionConfig,
    private val codec: Iso8583Codec = Iso8583Codec(),
    private val socketFactory: (NibssConnectionConfig) -> Socket = ::defaultSocket,
) : Closeable {

    private val mutex = Mutex()

    @Volatile
    private var socket: Socket? = null

    @Volatile
    private var input: InputStream? = null

    @Volatile
    private var output: OutputStream? = null

    suspend fun sendAndReceive(request: ISOMsg): ISOMsg = mutex.withLock {
        withContext(Dispatchers.IO) { sendAndReceiveBlocking(request) }
    }

    /**
     * Retries only when the request provably never reached the host. Responses
     * that were sent but not received are surfaced as [NibssExchangeException.ResponseNotReceived].
     */
    suspend fun sendAndReceiveWithRetry(request: ISOMsg): ISOMsg {
        var attempt = 1
        var backoff = config.initialBackoffMillis
        while (true) {
            try {
                return sendAndReceive(request)
            } catch (failure: NibssExchangeException.RequestNotSent) {
                if (attempt >= config.maxAttempts) throw failure
                delay(backoff.milliseconds)
                backoff = (backoff * 2).coerceAtMost(config.maxBackoffMillis)
                attempt += 1
            }
        }
    }

    override fun close() {
        synchronized(this) { closeQuietly() }
    }

    private fun sendAndReceiveBlocking(request: ISOMsg): ISOMsg {
        val payload = try {
            codec.pack(request)
        } catch (failure: Exception) {
            throw NibssExchangeException.ResponseMismatch("Could not pack request", failure)
        }

        val stream = output ?: connect()

        try {
            MessageFraming.write(stream, payload)
            stream.flush()
        } catch (failure: IOException) {
            closeQuietly()
            throw NibssExchangeException.RequestNotSent("Failed while sending request", failure)
        }

        val responsePayload = try {
            MessageFraming.read(input ?: throw IOException("Input stream is not open"))
        } catch (failure: IOException) {
            closeQuietly()
            throw NibssExchangeException.ResponseNotReceived("Failed waiting for response", failure)
        } ?: run {
            closeQuietly()
            throw NibssExchangeException.ResponseNotReceived("Connection closed before a response arrived")
        }

        val response = try {
            codec.unpack(responsePayload)
        } catch (failure: Exception) {
            closeQuietly()
            throw NibssExchangeException.ResponseMismatch("Could not parse response", failure)
        }

        verifyCorrelation(request, response)
        return response
    }

    private fun verifyCorrelation(request: ISOMsg, response: ISOMsg) {
        val requestStan = request.getString(11)
        val responseStan = response.getString(11)
        if (requestStan == null || responseStan == null || requestStan != responseStan) {
            closeQuietly()
            throw NibssExchangeException.ResponseMismatch(
                "STAN mismatch: request=$requestStan response=$responseStan"
            )
        }
    }

    private fun connect(): OutputStream {
        closeQuietly()
        val connected = try {
            socketFactory(config)
        } catch (failure: IOException) {
            throw NibssExchangeException.RequestNotSent("Could not connect to ${config.host}:${config.port}", failure)
        }
        socket = connected
        return try {
            connected.getInputStream().also { input = it }
            connected.getOutputStream().also { output = it }
        } catch (failure: IOException) {
            closeQuietly()
            throw NibssExchangeException.RequestNotSent("Could not open socket streams", failure)
        }
    }

    private fun closeQuietly() {
        runCatching { input?.close() }
        runCatching { output?.close() }
        runCatching { socket?.close() }
        input = null
        output = null
        socket = null
    }
}

private fun defaultSocket(config: NibssConnectionConfig): Socket = Socket().apply {
    connect(InetSocketAddress(config.host, config.port), config.connectTimeoutMillis)
    soTimeout = config.readTimeoutMillis
    tcpNoDelay = true
}

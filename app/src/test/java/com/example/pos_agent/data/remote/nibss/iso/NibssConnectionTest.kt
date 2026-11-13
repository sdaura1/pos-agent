package com.example.pos_agent.data.remote.nibss.iso

import java.net.ConnectException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.jpos.iso.ISOMsg
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NibssConnectionTest {

    private val codec = Iso8583Codec()

    @Test
    fun `sendAndReceive should correlate a response when the host replies`() = runBlocking {
        // Arrange
        val server = ServerSocket(0, 1, loopback())
        val serverFailure = AtomicReference<Throwable?>(null)
        val serverThread = thread(isDaemon = true) {
            try {
                server.accept().use { socket ->
                    val request = codec.read(socket.getInputStream())
                    codec.write(socket.getOutputStream(), networkResponse(request, "00"))
                }
            } catch (failure: Throwable) {
                serverFailure.set(failure)
            }
        }
        val connection = NibssConnection(config(server.localPort), codec)

        try {
            // Act
            val response = withTimeout(5_000) {
                connection.sendAndReceive(networkRequest())
            }

            // Assert
            assertEquals("00", response.getString(39))
            assertEquals("000001", response.getString(11))
            assertNull(serverFailure.get())
        } finally {
            connection.close()
            server.close()
            serverThread.join(1_000)
        }
    }

    @Test
    fun `sendAndReceive should reject a response when the STAN differs`() = runBlocking {
        // Arrange
        val server = ServerSocket(0, 1, loopback())
        val serverThread = thread(isDaemon = true) {
            server.accept().use { socket ->
                codec.read(socket.getInputStream())
                val response = networkResponse(networkRequest(), "00").apply {
                    set(11, "999999")
                }
                codec.write(socket.getOutputStream(), response)
            }
        }
        val connection = NibssConnection(config(server.localPort), codec)

        // Act
        val failure = try {
            withTimeout(5_000) { connection.sendAndReceive(networkRequest()) }
            null
        } catch (thrown: Throwable) {
            thrown
        } finally {
            connection.close()
            server.close()
            serverThread.join(1_000)
        }

        // Assert
        assertTrue(
            "Expected a protocol failure, got $failure",
            failure is NibssExchangeException.ResponseMismatch,
        )
    }

    @Test
    fun `sendAndReceive should report unknown when the host closes early`() = runBlocking {
        // Arrange
        val server = ServerSocket(0, 1, loopback())
        val serverThread = thread(isDaemon = true) {
            server.accept().use { socket ->
                codec.read(socket.getInputStream())
                // Close without responding.
            }
        }
        val connection = NibssConnection(config(server.localPort), codec)

        // Act
        val failure = try {
            withTimeout(5_000) { connection.sendAndReceive(networkRequest()) }
            null
        } catch (thrown: Throwable) {
            thrown
        } finally {
            connection.close()
            server.close()
            serverThread.join(1_000)
        }

        // Assert
        assertTrue(
            "Expected an unknown outcome, got $failure",
            failure is NibssExchangeException.ResponseNotReceived,
        )
    }

    @Test
    fun `sendAndReceive should report a safe failure when connect is refused`() = runBlocking {
        // Arrange
        val port = ServerSocket(0, 1, loopback()).use { it.localPort }
        val connection = NibssConnection(
            config(port, connectTimeoutMillis = 250),
            codec,
        )

        // Act
        val failure = try {
            withTimeout(5_000) { connection.sendAndReceive(networkRequest()) }
            null
        } catch (thrown: Throwable) {
            thrown
        } finally {
            connection.close()
        }

        // Assert
        assertTrue(
            "Expected a not-sent failure, got $failure",
            failure is NibssExchangeException.RequestNotSent,
        )
    }

    @Test
    fun `sendAndReceiveWithRetry should recover when the first connect fails`() = runBlocking {
        // Arrange
        val server = ServerSocket(0, 1, loopback())
        val serverThread = thread(isDaemon = true) {
            server.accept().use { socket ->
                val request = codec.read(socket.getInputStream())
                codec.write(socket.getOutputStream(), networkResponse(request, "00"))
            }
        }
        val attempts = AtomicInteger(0)
        val connection = NibssConnection(
            config(server.localPort).copy(maxAttempts = 2),
            codec,
        ) { settings ->
            if (attempts.incrementAndGet() == 1) {
                throw ConnectException("transient refusal")
            }
            socketTo(settings)
        }

        try {
            // Act
            val response = withTimeout(5_000) {
                connection.sendAndReceiveWithRetry(networkRequest())
            }

            // Assert
            assertEquals("00", response.getString(39))
            assertEquals(2, attempts.get())
        } finally {
            connection.close()
            server.close()
            serverThread.join(1_000)
        }
    }

    @Test
    fun `sendAndReceiveWithRetry should not resend when outcome is unknown`() = runBlocking {
        // Arrange
        val server = ServerSocket(0, 1, loopback())
        val serverThread = thread(isDaemon = true) {
            server.accept().use { socket ->
                codec.read(socket.getInputStream())
                // Close without responding: the host may have processed it.
            }
        }
        val connects = AtomicInteger(0)
        val connection = NibssConnection(
            config(server.localPort).copy(maxAttempts = 3),
            codec,
        ) { settings ->
            connects.incrementAndGet()
            socketTo(settings)
        }

        // Act
        val failure = try {
            withTimeout(5_000) { connection.sendAndReceiveWithRetry(networkRequest()) }
            null
        } catch (thrown: Throwable) {
            thrown
        } finally {
            connection.close()
            server.close()
            serverThread.join(1_000)
        }

        // Assert
        assertTrue(
            "Expected an unknown outcome, got $failure",
            failure is NibssExchangeException.ResponseNotReceived,
        )
        assertEquals("An unknown outcome must never be resent", 1, connects.get())
    }

    private fun loopback(): InetAddress = InetAddress.getByName("127.0.0.1")

    private fun socketTo(settings: NibssConnectionConfig): Socket = Socket().apply {
        connect(
            InetSocketAddress(settings.host, settings.port),
            settings.connectTimeoutMillis,
        )
        soTimeout = settings.readTimeoutMillis
    }

    private fun config(
        port: Int,
        connectTimeoutMillis: Int = 500,
    ) = NibssConnectionConfig(
        host = "127.0.0.1",
        port = port,
        connectTimeoutMillis = connectTimeoutMillis,
        readTimeoutMillis = 2_000,
        maxAttempts = 1,
        initialBackoffMillis = 10,
        maxBackoffMillis = 50,
    )

    private fun networkRequest(): ISOMsg = codec.newMessage("0800").apply {
        set(3, "9B0000")
        set(7, "1113221500")
        set(11, "000001")
        set(12, "221500")
        set(13, "1113")
        set(41, "TID00001")
        set(62, TaggedFieldCodec.encode(TaggedField("01", "1234567890")))
        set(64, "H".repeat(64))
    }

    private fun networkResponse(request: ISOMsg, responseCode: String): ISOMsg =
        codec.newMessage("0810").apply {
            set(7, "1113221501")
            set(11, request.getString(11))
            set(12, "221500")
            set(13, "1113")
            set(39, responseCode)
            set(41, "TID00001")
            set(64, "R".repeat(64))
        }
}

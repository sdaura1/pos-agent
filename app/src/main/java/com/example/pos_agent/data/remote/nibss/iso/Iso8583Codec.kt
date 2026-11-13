package com.example.pos_agent.data.remote.nibss.iso

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOPackager

/**
 * Packs and parses CTMS ISO 8583 messages. Framing is applied separately so the
 * codec stays usable for fixtures and offline tests.
 */
class Iso8583Codec(
    val packager: ISOPackager = Iso8583Packager.create(),
) {
    fun newMessage(mti: String): ISOMsg = ISOMsg().apply {
        this.packager = this@Iso8583Codec.packager
        setMTI(mti)
    }

    fun pack(message: ISOMsg): ByteArray {
        message.packager = packager
        return message.pack()
    }

    fun unpack(payload: ByteArray): ISOMsg = ISOMsg().apply {
        packager = this@Iso8583Codec.packager
        unpack(payload)
    }

    fun write(output: OutputStream, message: ISOMsg) {
        MessageFraming.write(output, pack(message))
    }

    fun read(input: InputStream): ISOMsg {
        val payload = MessageFraming.read(input) ?: throw EOFException("Connection closed before a frame arrived")
        return unpack(payload)
    }
}

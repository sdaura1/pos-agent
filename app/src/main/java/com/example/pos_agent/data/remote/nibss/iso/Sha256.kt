package com.example.pos_agent.data.remote.nibss.iso

import java.security.MessageDigest

/** SHA-256 digest rendered as uppercase hex. */
internal object Sha256 {

    fun hexDigest(bytes: ByteArray): String =
        HexCodec.encode(MessageDigest.getInstance("SHA-256").digest(bytes))
}

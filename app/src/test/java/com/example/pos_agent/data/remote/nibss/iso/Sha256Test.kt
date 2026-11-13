package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Test

class Sha256Test {

    @Test
    fun `matches the NIST vector for abc`() {
        assertEquals(
            "BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD",
            Sha256.hexDigest("abc".toByteArray(Charsets.US_ASCII)),
        )
    }

    @Test
    fun `matches the NIST vector for the empty input`() {
        assertEquals(
            "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855",
            Sha256.hexDigest(ByteArray(0)),
        )
    }
}

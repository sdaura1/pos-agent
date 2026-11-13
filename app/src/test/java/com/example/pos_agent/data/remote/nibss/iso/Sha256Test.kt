package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Test

class Sha256Test {

    @Test
    fun `hexDigest should match the NIST vector when hashing abc`() {
        // Arrange
        val input = "abc".toByteArray(Charsets.US_ASCII)

        // Act
        val digest = Sha256.hexDigest(input)

        // Assert
        assertEquals(
            "BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD",
            digest,
        )
    }

    @Test
    fun `hexDigest should match the NIST vector when hashing empty input`() {
        // Arrange
        val input = ByteArray(0)

        // Act
        val digest = Sha256.hexDigest(input)

        // Assert
        assertEquals(
            "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855",
            digest,
        )
    }
}

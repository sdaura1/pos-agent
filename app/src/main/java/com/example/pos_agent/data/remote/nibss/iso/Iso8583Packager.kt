package com.example.pos_agent.data.remote.nibss.iso

import org.jpos.iso.ISOPackager
import org.jpos.iso.packager.GenericPackager

/**
 * Loads the CTMS ASCII packager.
 *
 * `jpos.xml` is the single packager for the NIBSS dialect: 0200/0210,
 * 0420/0421/0430 and the 0800/0810 network-management messages. It lives in
 * `src/main/resources` so it is available to both the APK classloader and JVM
 * tests.
 */
object Iso8583Packager {

    private const val RESOURCE = "jpos.xml"

    fun create(): ISOPackager {
        val stream = Iso8583Packager::class.java.classLoader
            ?.getResourceAsStream(RESOURCE)
            ?: error("Missing ISO packager resource: $RESOURCE")
        return stream.use { GenericPackager(it) }
    }
}

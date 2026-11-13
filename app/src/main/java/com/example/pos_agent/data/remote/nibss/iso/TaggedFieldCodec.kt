package com.example.pos_agent.data.remote.nibss.iso

/** A DE62/DE63 tagged item: two-digit tag, three-digit length, value. */
data class TaggedField(
    val tag: String,
    val value: String,
)

/**
 * Encoder/decoder for the tagged payloads carried in DE62 and DE63.
 *
 * Format is `tag(2) + length(3) + value`, repeated. The same shape is used for
 * merchant data, timeouts, key serial numbers and EMV parameter downloads.
 */
object TaggedFieldCodec {

    fun encode(fields: List<TaggedField>): String = buildString {
        fields.forEach { field ->
            require(field.tag.length == 2 && field.tag.all(Char::isDigit)) {
                "Tag must be two digits: ${field.tag}"
            }
            require(field.value.length <= 999) {
                "Value for tag ${field.tag} is too long: ${field.value.length}"
            }
            append(field.tag)
            append(field.value.length.toString().padStart(3, '0'))
            append(field.value)
        }
    }

    fun encode(vararg fields: TaggedField): String = encode(fields.toList())

    fun decode(raw: String): List<TaggedField> {
        val fields = mutableListOf<TaggedField>()
        var index = 0
        while (index < raw.length) {
            require(index + 5 <= raw.length) {
                "Truncated tag header at $index"
            }
            val tag = raw.substring(index, index + 2)
            require(tag.all(Char::isDigit)) { "Non-numeric tag '$tag' at $index" }

            val lengthText = raw.substring(index + 2, index + 5)
            val length = lengthText.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid length '$lengthText' for tag $tag")

            val valueStart = index + 5
            val valueEnd = valueStart + length
            require(valueEnd <= raw.length) {
                "Value for tag $tag runs past the end (needs $length, has ${raw.length - valueStart})"
            }
            fields += TaggedField(tag = tag, value = raw.substring(valueStart, valueEnd))
            index = valueEnd
        }
        return fields
    }

    fun decodeToMap(raw: String): Map<String, String> =
        decode(raw).associate { it.tag to it.value }
}

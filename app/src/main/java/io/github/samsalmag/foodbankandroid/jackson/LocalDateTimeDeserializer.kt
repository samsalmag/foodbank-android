package io.github.samsalmag.foodbankandroid.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
        return LocalDateTime.parse(parser.valueAsString, formatter)
    }
}

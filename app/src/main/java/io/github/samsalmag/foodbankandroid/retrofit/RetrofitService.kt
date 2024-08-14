package io.github.samsalmag.foodbankandroid.retrofit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitService {

    private val objectMapper: ObjectMapper = ObjectMapper()
                                                .registerModule(JavaTimeModule())   // Add module to serialize/deserialize java.time

    private val ip = "192.168.1.20"
    private val port = "8080"

    fun get(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://$ip:$port")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }
}

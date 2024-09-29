package io.github.samsalmag.foodbankandroid.util

import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Response
import java.io.IOException

object RetrofitUtil {
    fun requestToString(response: Response<*>): String {
        return requestToString(response.raw().request().body())
    }

    // https://stackoverflow.com/a/35198633
    fun requestToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}

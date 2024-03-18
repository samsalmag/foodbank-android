package com.samsalek.foodbankandroid.retrofit;

import java.io.IOException;

import okhttp3.RequestBody;

import okio.Buffer;
import retrofit2.Response;

public class RetrofitUtil {

    private RetrofitUtil(){}

    public static String requestToString(final Response<?> response) {
        return requestToString(response.raw().request().body());
    }

    // https://stackoverflow.com/a/35198633
    public static String requestToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}

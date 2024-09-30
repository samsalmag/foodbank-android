package io.github.samsalmag.foodbankandroid

enum class ApiConnectivityStatus(private val statusText: String) {
    CONNECTED("Connected"),
    FAILED("Connection Failed"),
    CHECKING_CONNECTION("Checking..."),
    UNAVAILABLE("Unavailable");

    override fun toString(): String {
        return statusText
    }
}

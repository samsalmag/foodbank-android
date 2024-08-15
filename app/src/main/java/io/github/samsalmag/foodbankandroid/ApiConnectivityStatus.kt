package io.github.samsalmag.foodbankandroid

enum class ApiConnectivityStatus(val statusText: String) {
    CONNECTED("Connected"),
    FAILED("Connection Failed"),
    CHECKING_CONNECTION("Checking..."),
    UNAVAILABLE("Unavailable")
}

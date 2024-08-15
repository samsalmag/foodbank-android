package io.github.samsalmag.foodbankandroid

import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ApiConnectivityChecker {

    fun checkApiConnectionPeriodically(scope: CoroutineScope, intervalMillis: Long = 60000, onConnectivityChange: (ApiConnectivityStatus) -> Unit)  {
        scope.launch {
            while (true) {
                // Begin checking connection
                withContext(Dispatchers.Main) {
                    onConnectivityChange(ApiConnectivityStatus.CHECKING_CONNECTION)
                }
                val connectionStatus = checkApiConnection()
                // Update connection status
                withContext(Dispatchers.Main) {
                    onConnectivityChange(connectionStatus)
                }
                delay(intervalMillis)
            }
        }
    }

    private suspend fun checkApiConnection(): ApiConnectivityStatus {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<Void> = RetrofitService().foodbankApi.default().execute()

                // Returns true if the response code is 2xx
                if (response.isSuccessful) ApiConnectivityStatus.CONNECTED
                else ApiConnectivityStatus.FAILED
            }
            catch (e: Exception) {
                ApiConnectivityStatus.UNAVAILABLE
            }
        }
    }
}

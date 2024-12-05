package uz.excellentshoes.businesscalculation.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


open class BaseViewModel : ViewModel() {
    private val _networkError = MutableSharedFlow<Unit>()
    val networkError = _networkError.asSharedFlow()

    protected suspend fun <T> safeApiCall(
        networkStatus: StateFlow<Boolean>,
        block: suspend () -> T
    ): Result<T> = withContext(Dispatchers.IO) {
        if (!networkStatus.value) {
            _networkError.emit(Unit)
            return@withContext Result.failure(NoInternetException())
        }

        try {
            // Add ping test to verify actual internet connectivity
            if (!pingTest()) {
                _networkError.emit(Unit)
                return@withContext Result.failure(NoInternetException())
            }
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun pingTest(): Boolean = withContext(Dispatchers.IO) {
        try {
            val sock = Socket()
            sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}
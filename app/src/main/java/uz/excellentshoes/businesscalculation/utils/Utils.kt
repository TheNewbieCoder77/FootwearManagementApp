package uz.excellentshoes.businesscalculation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
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
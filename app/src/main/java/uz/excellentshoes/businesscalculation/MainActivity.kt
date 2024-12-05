package uz.excellentshoes.businesscalculation


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.utils.NetworkBannerView
import uz.excellentshoes.businesscalculation.utils.NetworkConnectivityObserver
import uz.excellentshoes.businesscalculation.utils.NetworkUtils
import android.provider.Settings
import android.util.Log
import uz.excellentshoes.businesscalculation.app.App

class MainActivity : AppCompatActivity(){
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private lateinit var networkBanner: NetworkBannerView
    private var isShowingBanner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkBanner = findViewById(R.id.networkBanner)
        networkConnectivityObserver = (application as App).networkConnectivityObserver

        setupNetworkMonitoring()
    }

    private fun setupNetworkMonitoring() {
        networkConnectivityObserver.startObserving()

        networkBanner.apply {
            setOnRetryClickListener {
                lifecycleScope.launch {
                    if (NetworkUtils.isInternetAvailable()) {
                        hide()
                    }
                }
            }
            setOnSettingsClickListener {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            setOnDismissClickListener {
                hide()
                isShowingBanner = false
            }
        }

        // Monitor network status changes
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkConnectivityObserver.networkStatus
                    .debounce(300) // Prevent rapid changes
                    .distinctUntilChanged()
                    .collect { isConnected ->
                        handleConnectivityChange(isConnected)
                    }
            }
        }

        // Periodic connectivity verification
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(10000) // Check every 10 seconds
                    verifyConnectivity()
                }
            }
        }
    }

    private suspend fun handleConnectivityChange(isConnected: Boolean) {
        if (!isConnected && !isShowingBanner) {
            // Double check with actual internet test
            if (!NetworkUtils.isInternetAvailable()) {
                networkBanner.show()
                isShowingBanner = true
            }
        } else if (isConnected && isShowingBanner) {
            networkBanner.hide()
            isShowingBanner = false
        }
    }

    private suspend fun verifyConnectivity() {
        val hasNetwork = NetworkUtils.isNetworkAvailable(this)
        val hasInternet = NetworkUtils.isInternetAvailable()

        if (!hasNetwork || !hasInternet) {
            if (!isShowingBanner) {
                networkBanner.show()
                isShowingBanner = true
            }
        } else if (isShowingBanner) {
            networkBanner.hide()
            isShowingBanner = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnectivityObserver.stopObserving()
    }
}
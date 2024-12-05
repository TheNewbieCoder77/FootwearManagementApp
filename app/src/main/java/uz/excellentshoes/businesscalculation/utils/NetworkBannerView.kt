package uz.excellentshoes.businesscalculation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.button.MaterialButton
import uz.excellentshoes.businesscalculation.R

class NetworkBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: View
    private val buttonRetry: MaterialButton
    private val buttonSettings: MaterialButton
    private val buttonDismiss: ImageButton
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        binding = LayoutInflater.from(context).inflate(R.layout.view_network_banner, this, true)

        // Initialize views
        buttonRetry = binding.findViewById(R.id.buttonRetry)
        buttonSettings = binding.findViewById(R.id.buttonSettings)
        buttonDismiss = binding.findViewById(R.id.buttonDismiss)

        setupRetryButton()
    }

    private fun setupRetryButton() {
        buttonRetry.setOnClickListener {
            checkConnection()
        }
    }

    fun show() {
        if (visibility == View.VISIBLE) return

        visibility = View.VISIBLE
        translationY = -height.toFloat()
        animate()
            .translationY(0f)
            .setDuration(500)
            .setInterpolator(FastOutSlowInInterpolator())
            .start()
    }

    fun hide() {
        if (visibility == View.GONE) return

        animate()
            .translationY(-height.toFloat())
            .setDuration(500)
            .setInterpolator(FastOutSlowInInterpolator())
            .withEndAction { visibility = View.GONE }
            .start()
    }

    private fun checkConnection() {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        if (isConnected) {
            hide()
        }
    }

    fun setOnRetryClickListener(listener: () -> Unit) {
        buttonRetry.setOnClickListener { listener() }
    }

    fun setOnSettingsClickListener(listener: () -> Unit) {
        buttonSettings.setOnClickListener { listener() }
    }

    fun setOnDismissClickListener(listener: () -> Unit) {
        buttonDismiss.setOnClickListener { listener() }
    }
}
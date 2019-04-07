package org.dukecon.android.ui.features.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import org.dukecon.domain.features.networking.NetworkUtils

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class LolipopConnectionStateMonitor(val context: Context, val networkUtils: NetworkUtils)
    : ConnectivityManager.NetworkCallback(), NetworkOfflineChecker {

    private val networkRequest: NetworkRequest

    init {
        networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
    }

    override fun enable() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun disable() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        checkoForCaptivePortal()
    }

    override fun onLost(network: Network) {
        checkoForCaptivePortal()
    }

    private fun checkoForCaptivePortal() {
        Thread(Runnable {
            networkUtils.isConnectedToCaptivePortal = HTTP204CaptivePortalChecker.isCaptivePortal
        }).start()
    }
}
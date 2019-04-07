package org.dukecon.android.ui.features.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import mu.KotlinLogging
import org.dukecon.domain.features.networking.NetworkUtils
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

private val logger = KotlinLogging.logger {}

/**
 * Class containing some static utility methods.
 */
class AndroidNetworkUtils @Inject
constructor(private val context: Context) : NetworkUtils {
    override val isOffline: Boolean
        get() = !isInternetConected

    private val connectedToCaptivePortal = AtomicBoolean(false)

    init {
        Thread(Runnable { isConnectedToCaptivePortal = HTTP204CaptivePortalChecker.isCaptivePortal }).start()
    }

    protected val isCaptivePortal: Boolean
        get() = connectedToCaptivePortal.get()

    override val isWiFiConected: Boolean
        get() {

            val wifi = getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (wifi != null) {

                val wifiDetais = wifi.detailedState
                val fineGrainedConnected = wifiDetais == NetworkInfo.DetailedState.CONNECTED && !isCaptivePortal

                return wifi.isConnected && fineGrainedConnected
            } else {
                return false
            }
        }

    override val isMobileNetworkConected: Boolean
        get() {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (network != null) {

                val networkDetais = network.detailedState
                val fineGrainedConnected = networkDetais == NetworkInfo.DetailedState.CONNECTED

                return network.isConnected && fineGrainedConnected
            } else {
                return false
            }
        }

    override val isInternetConected: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            val connectedToNetwork = netInfoMob != null && netInfoMob.isConnectedOrConnecting
            val wifiConnected = isWiFiConected

            return (connectedToNetwork || wifiConnected) && !isCaptivePortal
        }

    override var isConnectedToCaptivePortal: Boolean
        get() = connectedToCaptivePortal.get()
        set(newValue) {
            logger.debug { "Connected to captive portal=" + if (newValue) "true" else "false" }
            this.connectedToCaptivePortal.set(newValue)
        }

    protected fun getNetworkInfo(networkType: Int): NetworkInfo? {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connManager.getNetworkInfo(networkType)
    }
}

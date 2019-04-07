package org.dukecon.android.ui.features.networking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mu.KotlinLogging
import org.dukecon.domain.features.networking.NetworkUtils

private val logger = KotlinLogging.logger {}

class ConnectionStateBroadcastReceiver(val networkUtils: NetworkUtils) : BroadcastReceiver() {

    private fun checkoForCaptivePortal(context: Context) {
        Thread(Runnable {
            networkUtils.isConnectedToCaptivePortal = HTTP204CaptivePortalChecker.isCaptivePortal
        }).start()
    }

    override fun onReceive(context: Context, intent: Intent) {
        logger.debug { "connection changed" }

        checkoForCaptivePortal(context)
    }
}

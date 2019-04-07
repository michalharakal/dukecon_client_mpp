package org.dukecon.android.ui.features.networking

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import org.dukecon.domain.features.networking.NetworkUtils

class ConnectionStateMonitor(private val context: Context, networkUtils: NetworkUtils) : NetworkOfflineChecker {

    internal var receiver: ConnectionStateBroadcastReceiver

    init {
        receiver = ConnectionStateBroadcastReceiver(networkUtils)
    }

    override fun enable() {
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun disable() {
        context.unregisterReceiver(receiver)
    }
}
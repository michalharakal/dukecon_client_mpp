package org.dukecon.android.ui.features.networking

import java.io.IOException
import java.net.*

object HTTP204CaptivePortalChecker {

    private val SOCKET_TIMEOUT_MS = 10000

    private val DEFAULT_SERVER = "clients3.google.com"

    val isCaptivePortal: Boolean
        get() {
            val server = lookupHost(DEFAULT_SERVER)
            return isCaptivePortal(server)
        }

    /**
     * Do a URL fetch on a known server to see if we get the data we expect
     */
    private fun isCaptivePortal(server: InetAddress?): Boolean {
        var urlConnection: HttpURLConnection? = null

        if (server == null) {
            return false
        }

        if (server.hostAddress == null) {
            return false
        }

        val mUrl = "http://" + server.hostAddress + "/generate_204"
        try {
            val url = URL(mUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.instanceFollowRedirects = false
            urlConnection.connectTimeout = SOCKET_TIMEOUT_MS
            urlConnection.readTimeout = SOCKET_TIMEOUT_MS
            urlConnection.useCaches = false
            urlConnection.inputStream
            // we got a valid response, but not from the real google
            return urlConnection.responseCode != 204
        } catch (e: IOException) {
            return false
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }
    }

    private fun lookupHost(hostname: String): InetAddress? {
        val inetAddress: Array<InetAddress>
        try {
            inetAddress = InetAddress.getAllByName(hostname)
        } catch (e: UnknownHostException) {
            return null
        }

        for (a in inetAddress) {
            if (a is Inet4Address)
                return a
        }
        return null
    }
}

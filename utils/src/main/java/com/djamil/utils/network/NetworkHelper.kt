package com.djamil.utils.network

import android.content.Context
import android.net.ConnectivityManager
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 5/18/20
 */
class NetworkHelper {

    companion object {

        @JvmStatic
        fun isConnected(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }


        fun isURLReachable(urlReachable: String): Boolean {
            val url = URL(urlReachable)
            val socket = Socket()
            socket.soTimeout = 200
            socket.connect(InetSocketAddress(url.host, url.port), 200)
            val isConect = socket.isConnected
            socket.close()
            return isConect
        }

        fun isURLReachable2(urlReachable: String): Boolean {
            val url = URL(urlReachable)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val code: Int = connection.responseCode

            return code == 200
        }
    }
}
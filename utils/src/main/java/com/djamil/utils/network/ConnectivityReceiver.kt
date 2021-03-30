package com.djamil.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 5/18/20
 */
class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(
                NetworkHelper.isConnected(
                    context
                )
            )
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}
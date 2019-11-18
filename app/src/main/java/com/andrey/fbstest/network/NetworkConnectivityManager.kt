package com.andrey.fbstest.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkConnectivityManager {
    private val networkChangeReceiver: BroadcastReceiver

    init {
        this.networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val networkState =
                    getConnectivityStatus(
                        context
                    )
            }
        }
    }

    companion object {
        private val TYPE_WIFI = 1
        private val TYPE_MOBILE = 2
        private val TYPE_NOT_CONNECTED = 0

        fun getConnectivityStatus(context: Context): Int {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI

                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE
            }

            return TYPE_NOT_CONNECTED
        }
    }
}
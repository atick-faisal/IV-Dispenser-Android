package dev.atick.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger

class NetworkUtils(context: Context) {

    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val _isInternetAvailable = MutableLiveData<Event<Boolean>>()
    val isInternetAvailable: LiveData<Event<Boolean>>
        get() = _isInternetAvailable

    private val _isWiFiAvailable = MutableLiveData<Event<Boolean>>()
    val isWiFiAvailable: LiveData<Event<Boolean>>
        get() = _isWiFiAvailable

    init {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isInternetAvailable.postValue(Event(true))
                _isWiFiAvailable.postValue(Event(!connectivityManager.isActiveNetworkMetered))
                Logger.i("NETWORK CONNECTED")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                Logger.i("LOSING NETWORK CONNECTION ... ")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isInternetAvailable.postValue(Event(false))
                _isWiFiAvailable.postValue(Event(false))
                Logger.i("NETWORK CONNECTION LOST")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Logger.i("NETWORK UNAVAILABLE")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Logger.i("NETWORK TYPE CHANGED")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                Logger.i("LINK PROPERTIES CHANGED")
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                Logger.i("BLOCKED STATUS CHANGED")
            }
        })
    }
}
package com.gourmet.hotpepper.location


import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationRepository(
    val context: Context
) {

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback

    fun getCurrentLocation(callback: LocationDataSource.SearchLocationCallback) {
        fusedLocationClient = FusedLocationProviderClient(context)

        // どのような取得方法を要求
        val locationRequest = LocationRequest().apply {
            // 精度重視(電力大)と省電力重視(精度低)を両立するため2種類の更新間隔を指定
            // 今回は公式のサンプル通りにする。
            interval = 10000                                   // 最遅の更新間隔(但し正確ではない。)
            fastestInterval = 5000                             // 最短の更新間隔
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY  // 精度重視
        }

        // コールバック
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                val location = locationResult?.lastLocation ?: return
                callback.onLocationSearched(location.latitude, location.longitude)
                removeLocationCallback()
            }
        }

        // 位置情報を更新
        // TODO パーミッションのチェックをする必要がある
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun removeLocationCallback() {

        if (locationCallback != null) {

            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    companion object {

        private var INSTANCE: LocationRepository? = null

        @JvmStatic fun getInstance(context: Context): LocationRepository {
            return INSTANCE ?: LocationRepository(context)
                .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}
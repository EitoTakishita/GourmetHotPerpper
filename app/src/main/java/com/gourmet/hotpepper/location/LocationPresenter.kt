package com.gourmet.hotpepper.location

import android.os.Handler

class LocationPresenter(
    private val locationRepository: LocationRepository,
    private val locationView: LocationContract.View
) : LocationContract.Presenter {

    init {
        //Presenterを指定
        locationView.presenter = this
    }

    override fun getLocation() {
        searchCurrentLocation()
    }

    override fun searchShopList(freeword: String) {
        // NOP
    }

    override fun searchPickupInTokyo(middleAreaCode: String) {
        // NOP
    }

    override fun searchShopListByCurrentLocation(keyword: String, latitude: Double, longitude: Double) {
        // NOP
    }

    override fun removeLocationCallback() {
        locationRepository.removeLocationCallback()
    }

    private fun searchCurrentLocation() {
        locationView.setSearchText(false)

        Handler().postDelayed({

            locationRepository.getCurrentLocation(object : LocationDataSource.SearchLocationCallback {
                override fun onLocationSearched(lat: Double, lng: Double) {
                    locationView.setSearchText(true)
                    locationView.showResult(lat, lng)
                }
            })
        }, 2000)
    }
}
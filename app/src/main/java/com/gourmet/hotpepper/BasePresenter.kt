package com.gourmet.hotpepper

interface BasePresenter {

    fun getLocation()
    fun removeLocationCallback()
    fun searchShopList(freeword: String)
    fun searchPickupInTokyo(middleAreaCode: String)
    fun searchShopListByCurrentLocation(keyword: String, latitude: Double, longitude: Double)
}
package com.gourmet.hotpepper.location

interface LocationDataSource {

    interface SearchLocationCallback {

        fun onLocationSearched(lat: Double, lng: Double)
    }
}
package com.gourmet.hotpepper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Results {

    @SerializedName("results_start")
    @Expose
    var resultsStart: Int? = null
    @SerializedName("results_returned")
    @Expose
    var resultsReturned: String? = null
    @SerializedName("api_version")
    @Expose
    var apiVersion: String? = null
    @SerializedName("shop")
    @Expose
    var shop: List<Shop>? = null
    @SerializedName("results_available")
    @Expose
    var resultsAvailable: Int? = null

}

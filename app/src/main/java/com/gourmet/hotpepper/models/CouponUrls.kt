package com.gourmet.hotpepper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CouponUrls {

    @SerializedName("sp")
    @Expose
    var sp: String? = null
    @SerializedName("pc")
    @Expose
    var pc: String? = null

}

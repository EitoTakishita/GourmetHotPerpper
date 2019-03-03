package com.gourmet.hotpepper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Photo {

    @SerializedName("mobile")
    @Expose
    var mobile: Mobile? = null
    @SerializedName("pc")
    @Expose
    var pc: Pc? = null

}

package com.gourmet.hotpepper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Mobile {

    @SerializedName("l")
    @Expose
    var l: String? = null
    @SerializedName("s")
    @Expose
    var s: String? = null

}

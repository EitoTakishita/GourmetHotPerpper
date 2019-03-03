package com.gourmet.hotpepper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SmallArea {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}

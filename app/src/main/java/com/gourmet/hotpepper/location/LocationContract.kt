package com.gourmet.hotpepper.location

import com.gourmet.hotpepper.BasePresenter
import com.gourmet.hotpepper.BaseView

interface LocationContract {

    interface View : BaseView<Presenter> {

        // StartPresetnterから呼ばれる
        fun setSearchText(isFound: Boolean)

        fun showResult(lat: Double, lng: Double)
    }

    interface Presenter : BasePresenter
}
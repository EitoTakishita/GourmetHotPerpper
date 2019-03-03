package com.gourmet.hotpepper.shoplist

import com.gourmet.hotpepper.BasePresenter
import com.gourmet.hotpepper.BaseView

interface ShopListContract {

    interface View : BaseView<Presenter> {

        // ShopListPresetnterから呼ばれる
        fun showShopList(shopList: MutableList<ShopInfo>)
    }

    interface Presenter : BasePresenter
}
package com.gourmet.hotpepper.swipe

import com.gourmet.hotpepper.BasePresenter
import com.gourmet.hotpepper.BaseView
import com.gourmet.hotpepper.shoplist.ShopInfo

interface ShopSwipeContract {

    interface View : BaseView<Presenter> {

        fun showShopInTokyo(shopList: MutableList<ShopInfo>)
    }

    interface Presenter : BasePresenter
}
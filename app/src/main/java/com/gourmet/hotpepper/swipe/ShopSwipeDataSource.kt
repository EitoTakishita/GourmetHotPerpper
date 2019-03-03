package com.gourmet.hotpepper.swipe

import com.gourmet.hotpepper.shoplist.ShopInfo

interface ShopSwipeDataSource {

    interface PickupShopListCallback {

        fun onPickupShopSearched(shopList: MutableList<ShopInfo>)
    }
}
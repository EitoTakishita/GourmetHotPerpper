package com.gourmet.hotpepper.shoplist

interface ShopListDataSource {

    interface SearchShopListCallback {

        fun onShopSearched(shopList: MutableList<ShopInfo>)
    }
}
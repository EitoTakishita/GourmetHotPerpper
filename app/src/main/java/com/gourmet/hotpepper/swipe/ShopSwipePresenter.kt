package com.gourmet.hotpepper.swipe

import com.gourmet.hotpepper.shoplist.ShopInfo

class ShopSwipePresenter(
    private val shopSwipeRepository: ShopSwipeRepository,
    private val shopSwipeView: ShopSwipeContract.View
) : ShopSwipeContract.Presenter {

    init {
        //Presenterを指定
        shopSwipeView.presenter = this
    }

    override fun getLocation() {
        // NOP
    }

    override fun removeLocationCallback() {
        // NOP
    }

    override fun searchShopList(freeword: String) {
        // NOP
    }

    override fun searchPickupInTokyo(middleAreaCode: String) {
        getPickupInTokyo(middleAreaCode)
    }

    override fun searchShopListByCurrentLocation(keyword: String, latitude: Double, longitude: Double) {
        // NOP
    }

    private fun getPickupInTokyo(middleAreaCode: String) {
        // 検索中と表示
        shopSwipeRepository.fetchShopListInTokyo(object : ShopSwipeDataSource.PickupShopListCallback {
            override fun onPickupShopSearched(shopList: MutableList<ShopInfo>) {
                shopSwipeView.showShopInTokyo(shopList)
            }
        }, middleAreaCode)
    }
}
package com.gourmet.hotpepper.shoplist

class ShopListPresenter(
    private val shopListRepository: ShopListRepository,
    private val shopListView: ShopListContract.View
) : ShopListContract.Presenter {

    init {
        //Presenterを指定
        shopListView.presenter = this
    }

    override fun getLocation() {
        // NOP
    }

    override fun removeLocationCallback() {
        // NOP
    }

    override fun searchPickupInTokyo(middleAreaCode: String) {
        // NOP
    }

    override fun searchShopList(freeword: String) {

        getShopListByFreeWord(freeword)
    }

    override fun searchShopListByCurrentLocation(keyword: String, latitude: Double, longitude: Double) {
        getShopListAtCurrentLocation(keyword, latitude, longitude)
    }

    private fun getShopListByFreeWord(freeword: String) {
        // 検索中と表示
        shopListRepository.fetchShopListByFreeWord(object : ShopListDataSource.SearchShopListCallback {
            override fun onShopSearched(shopList: MutableList<ShopInfo>) {
                shopListView.showShopList(shopList)
            }
        }, freeword)
    }

    private fun getShopListAtCurrentLocation(keyword: String, latitude: Double, longitude: Double) {
        // 検索中と表示
        shopListRepository.fetchShopListAtCurrentLocation(object : ShopListDataSource.SearchShopListCallback {
            override fun onShopSearched(shopList: MutableList<ShopInfo>) {
                shopListView.showShopList(shopList)
            }
        }, keyword, latitude, longitude)
    }
}
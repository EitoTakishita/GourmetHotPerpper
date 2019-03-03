package com.gourmet.hotpepper.shoplist

class ShopInfo {

    private var shopName: String? = ""
    private var shopPhoto: String? = ""
    private var shopUrl: String? = ""
    private var shopCatch: String? = ""
    private var shopBudget: String? = ""

    fun getShopName(): String? {
        return shopName
    }

    fun setShopName(shopName: String?) {
        this.shopName = shopName
    }

    fun getPhoto(): String? {
        return shopPhoto
    }

    fun setPhoto(photo: String?) {
        this.shopPhoto = photo
    }

    fun getShopUrl(): String? {
        return shopUrl
    }

    fun setShopUrl(shopUrl: String?) {
        this.shopUrl = shopUrl
    }

    fun getShopCatch(): String? {
        return shopCatch
    }

    fun setShopCatch(shopCatch: String?) {
        this.shopCatch = shopCatch
    }

    fun getShopBudget(): String? {
        return shopBudget
    }

    fun setShopBudget(shopBudget: String?) {
        this.shopBudget = shopBudget
    }
}
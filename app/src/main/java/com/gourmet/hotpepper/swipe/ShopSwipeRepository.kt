package com.gourmet.hotpepper.swipe

import android.content.Context
import android.util.Log
import com.gourmet.hotpepper.models.Example
import com.gourmet.hotpepper.service.createService
import com.gourmet.hotpepper.shoplist.ShopInfo
import com.gourmet.hotpepper.util.HotPepperUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopSwipeRepository(
    val context: Context
) {

    private val hotPepperService by lazy { createService() }

    fun fetchShopListInTokyo(callback: ShopSwipeDataSource.PickupShopListCallback, middleAreaCode: String) {

        hotPepperService.getGourmetDataInTokyo(
            HotPepperUtil.hotPepperKey, middleAreaCode, HotPepperUtil.pickupCount, HotPepperUtil.recommendation, HotPepperUtil.format)
            .enqueue(object : Callback<Example> {
                override fun onFailure(call: Call<Example>, t: Throwable) {
                    Log.d("fetchShopListInTokyo", "Takishtia onFailure")
                }

                override fun onResponse(call: Call<Example>, response: Response<Example>) {
                    if (response.isSuccessful) {
                        getPickupShopList(response, callback)
                    }
                    Log.d("fetchShopListInTokyo", "Takishtia response code:" + response.code())
                    Log.d("fetchShopListInTokyo", "Takishtia response errorBody:" + response.errorBody())
                }
            })
    }

    private fun getPickupShopList(response: Response<Example>, callback: ShopSwipeDataSource.PickupShopListCallback) {

        val list = response.body()?.results?.shop
        list?.let {
            if (it.isEmpty()) {
                callback.onPickupShopSearched(mutableListOf())
                return
            }

            val shopList: MutableList<ShopInfo> = mutableListOf()
            for (shopData in it) {

                val shopInfo = ShopInfo()
                shopInfo.setShopName(shopData.name)
                shopInfo.setPhoto(shopData.photo?.pc?.l)
                shopInfo.setShopUrl(shopData.urls?.pc)
                shopInfo.setShopCatch(shopData.genre?.catch)
                shopList.add(shopInfo)
            }

            callback.onPickupShopSearched(shopList)
        }
    }

    companion object {

        private var INSTANCE: ShopSwipeRepository? = null

        @JvmStatic fun getInstance(context: Context): ShopSwipeRepository {
            return INSTANCE ?: ShopSwipeRepository(context)
                .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}
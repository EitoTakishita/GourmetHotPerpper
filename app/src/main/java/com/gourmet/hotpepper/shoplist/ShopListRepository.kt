package com.gourmet.hotpepper.shoplist

import android.content.Context
import android.util.Log
import com.gourmet.hotpepper.models.Example
import com.gourmet.hotpepper.service.createService
import com.gourmet.hotpepper.util.HotPepperUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopListRepository(
    val context: Context
) {

    private val hotPepperService by lazy { createService() }

    fun fetchShopListByFreeWord(callback: ShopListDataSource.SearchShopListCallback, keyword: String) {

        hotPepperService.getGourmetDataByFreeWord(
            HotPepperUtil.hotPepperKey, keyword, HotPepperUtil.showCount, HotPepperUtil.recommendation, HotPepperUtil.format)
            .enqueue(object : Callback<Example> {
                override fun onFailure(call: Call<Example>, t: Throwable) {
                    Log.d("fetchShopListByFreeWord", "Takishtia onFailure")
                }

                override fun onResponse(call: Call<Example>, response: Response<Example>) {
                    if (response.isSuccessful) {
                        getShopList(response, callback)
                    }
                    Log.d("fetchShopListByFreeWord", "Takishtia response code:" + response.code())
                    Log.d("fetchShopListByFreeWord", "Takishtia response errorBody:" + response.errorBody())
                }
            })
    }

    fun fetchShopListAtCurrentLocation(callback: ShopListDataSource.SearchShopListCallback, keyword: String, latitude: Double, longitude: Double) {

        Log.d("fetchCurrentLocation", "Takishtia keyword = " + keyword)
        hotPepperService.getGourmetDataByCurrentLocation(
            HotPepperUtil.hotPepperKey, keyword, latitude, longitude, HotPepperUtil.showCount, HotPepperUtil.recommendation, HotPepperUtil.format)
            .enqueue(object : Callback<Example> {
            override fun onFailure(call: Call<Example>, t: Throwable) {
                Log.d("fetchCurrentLocation", "Takishtia onFailure")
            }

            override fun onResponse(call: Call<Example>, response: Response<Example>) {
                if (response.isSuccessful) {
                    getShopList(response, callback)
                }
                Log.d("fetchCurrentLocation", "Takishtia response code:" + response.code())
                Log.d("fetchCurrentLocation", "Takishtia  errorBody:" + response.errorBody())
            }
        })
    }

    private fun getShopList(response: Response<Example>, callback: ShopListDataSource.SearchShopListCallback) {

        val list = response.body()?.results?.shop
        list?.let {
            if (it.isEmpty()) {
                callback.onShopSearched(mutableListOf())
                return
            }

            val shopList: MutableList<ShopInfo> = mutableListOf()
            for (shopData in it) {

                val shopInfo = ShopInfo()
                shopInfo.setShopName(shopData.name)
                shopInfo.setPhoto(shopData.photo?.pc?.l)
                shopInfo.setShopUrl(shopData.urls?.pc)
                shopInfo.setShopCatch(shopData.genre?.catch)
                shopInfo.setShopBudget(shopData.budget?.name)
                shopList.add(shopInfo)
            }

            callback.onShopSearched(shopList)
        }
    }

    companion object {

        private var INSTANCE: ShopListRepository? = null

        @JvmStatic fun getInstance(context: Context): ShopListRepository {
            return INSTANCE ?: ShopListRepository(context)
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
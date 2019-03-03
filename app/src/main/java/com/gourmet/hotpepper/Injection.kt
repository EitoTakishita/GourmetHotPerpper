package com.gourmet.hotpepper

import android.content.Context
import com.gourmet.hotpepper.location.LocationRepository
import com.gourmet.hotpepper.shoplist.ShopListRepository
import com.gourmet.hotpepper.swipe.ShopSwipeRepository

object Injection {



    fun provideShopSwipeRepository(context: Context): ShopSwipeRepository {

        return ShopSwipeRepository.getInstance(context)
    }

    fun destroyShopSwipeRepository() {

        return ShopSwipeRepository.destroyInstance()
    }

    fun provideLocationRepository(context: Context): LocationRepository {

        return LocationRepository.getInstance(context)
    }

    fun destroyLocationRepository() {

        return LocationRepository.destroyInstance()
    }

    fun provideShopListRepository(context: Context): ShopListRepository {

        return ShopListRepository.getInstance(context)
    }

    fun destroyShopListRepository() {

        return ShopListRepository.destroyInstance()
    }
}

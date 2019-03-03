package com.gourmet.hotpepper.shoplist

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.gourmet.hotpepper.Injection
import com.gourmet.hotpepper.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ShopListActivity : AppCompatActivity() {

    private val KEY_LAT = "KEY_LAT"
    private val KEY_LNG = "KEY_LNG"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoplist)

        val latitude = intent.extras.getDouble(KEY_LAT)
        val longitude = intent.extras.getDouble(KEY_LNG)


        val shopListFragment = supportFragmentManager
            .findFragmentById(R.id.shopListContainer) as ShopListFragment?
            ?: ShopListFragment.newInstance(latitude, longitude, "居酒屋", "デート", "立ち飲み").also {
                replaceFragmentInActivity(it, R.id.shopListContainer)
            }

        ShopListPresenter(
            Injection.provideShopListRepository(applicationContext), shopListFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        Injection.destroyShopListRepository()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
        supportFragmentManager.transact {
            replace(frameId, fragment)
        }
    }

    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.addToBackStack(null).commit()
    }
}
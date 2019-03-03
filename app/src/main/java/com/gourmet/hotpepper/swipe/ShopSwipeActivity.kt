package com.gourmet.hotpepper.swipe

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

class ShopSwipeActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_swipe)

        supportActionBar?.hide()

        val shopSwipeFragment = supportFragmentManager
            .findFragmentById(R.id.shopSwipeContainer) as ShopSwipeFragment?
            ?: ShopSwipeFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.shopSwipeContainer)
            }

        ShopSwipePresenter(
            Injection.provideShopSwipeRepository(applicationContext), shopSwipeFragment)
    }

    override fun onDestroy() {

        super.onDestroy()
        Injection.destroyShopSwipeRepository()
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
        }.commit()
    }
}
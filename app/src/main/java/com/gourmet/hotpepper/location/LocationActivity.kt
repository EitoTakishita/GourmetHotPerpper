package com.gourmet.hotpepper.location

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

class LocationActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        supportActionBar?.hide()

        val locationFragment = supportFragmentManager
            .findFragmentById(R.id.locationContainer) as LocationFragment?
            ?: LocationFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.locationContainer)
            }

        LocationPresenter(
            Injection.provideLocationRepository(applicationContext), locationFragment)
    }

    override fun onDestroy() {

        super.onDestroy()
        Injection.destroyLocationRepository()
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
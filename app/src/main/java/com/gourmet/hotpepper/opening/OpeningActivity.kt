package com.gourmet.hotpepper.opening

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.gourmet.hotpepper.R
import com.gourmet.hotpepper.swipe.ShopSwipeActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class OpeningActivity: AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        supportActionBar?.hide()

        Handler().postDelayed({

            val intent = Intent(applicationContext, ShopSwipeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.in_right, R.anim.out_left)
        }, 3000)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
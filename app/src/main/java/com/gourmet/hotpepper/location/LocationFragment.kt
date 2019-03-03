package com.gourmet.hotpepper.location

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gourmet.hotpepper.R
import com.gourmet.hotpepper.shoplist.ShopListActivity
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class LocationFragment : Fragment(), LocationContract.View {

    private lateinit var searchTextView: TextView

    override lateinit var presenter: LocationContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        container?.removeAllViews()
        var root = inflater.inflate(R.layout.fragment_location, container, false)
        searchTextView = root.findViewById(R.id.searchText)
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.getLocation()
    }

    override fun onPause() {
        super.onPause()
        presenter.removeLocationCallback()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.removeLocationCallback()
    }

    override fun setSearchText(isFound: Boolean) {
        if (isFound) {
            searchTextView.text = getString(R.string.current_location_found)
        } else {
            searchTextView.text = getString(R.string.searching_current_location)
        }
    }

    override fun showResult(lat: Double, lng: Double) {

        val intent = Intent(context, ShopListActivity::class.java)
        intent.putExtra("KEY_LAT", lat)
        intent.putExtra("KEY_LNG", lng)
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.in_right, R.anim.out_left)
    }

    companion object {

        fun newInstance(): LocationFragment {
            return LocationFragment()
        }
    }
}
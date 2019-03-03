package com.gourmet.hotpepper.swipe

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.gourmet.hotpepper.R
import com.gourmet.hotpepper.location.LocationActivity
import com.gourmet.hotpepper.shoplist.ShopInfo
import com.gourmet.hotpepper.util.HotPepperUtil
import com.kidach1.tinderswipe.model.CardModel
import com.kidach1.tinderswipe.view.CardContainer
import com.kidach1.tinderswipe.view.SimpleCardStackAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import kotlin.random.Random

class ShopSwipeFragment : Fragment(), ShopSwipeContract.View {

    override lateinit var presenter: ShopSwipeContract.Presenter

    private var cardCount = 0
    private var randomIndex = 0

    private lateinit var closeButton: Button
    private lateinit var pickupTextView: TextView
    private lateinit var pickupCountTextView: TextView
    private lateinit var cardContainer: CardContainer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        val root = inflater.inflate(R.layout.fragment_shop_swipe, container, false)
        closeButton = root.findViewById(R.id.close_btn)
        closeButton.setOnClickListener {

            showLocationActivity()
        }

        pickupTextView = root.findViewById(R.id.pickup_area_text)

        val arrayArea = resources.getStringArray(R.array.middle_area_array)
        randomIndex = Random.nextInt(arrayArea.size)
        var index = randomIndex
        val middleArea = arrayArea[index - 1]
        pickupTextView.text = getString(R.string.pickup_area, middleArea)

        pickupCountTextView = root.findViewById(R.id.pickup_count_text)

        cardContainer = root.findViewById(R.id.cardContainer)
        searchPickupInTokyo()

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                showLocationActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showShopInTokyo(shopList: MutableList<ShopInfo>) {
        pickupCountTextView.text = getString(R.string.pickup_count, shopList.size, shopList.size)

        val cardAdapter = SimpleCardStackAdapter(context!!)
        cardCount = shopList.size
        for (i in 0 until shopList.size) {

            val shopInfo = shopList[i]
            val shopName = shopInfo.getShopName()
            val description = shopInfo.getShopCatch()
            val imageUrl = shopInfo.getPhoto()
            val cardModel = CardModel(shopName!!, description!!, imageUrl!!)

            val shopUrl = shopInfo.getShopUrl()
            addDismissListener(cardModel, shopUrl!!, shopList.size)
            cardAdapter.add(cardModel)
        }

        cardContainer.adapter = cardAdapter
    }

    private fun searchPickupInTokyo() {

        val arrayAreaCode = resources.getStringArray(R.array.middle_area_code_array)
        var index = randomIndex
        val middleAreaCode = arrayAreaCode[index - 1]
        presenter.searchPickupInTokyo(middleAreaCode)
    }

    private fun addDismissListener(cardModel: CardModel, shopUrl: String, shopListSize: Int) {
        cardModel.onCardDismissedListener = object : CardModel.OnCardDismissedListener {
            override fun onLike(callback: CardContainer.OnLikeListener) {
                openDialog(callback, shopUrl)
            }

            override fun onDislike() {
                cardCount--
                pickupCountTextView.text = getString(R.string.pickup_count, cardCount, shopListSize)
                if (cardCount == 0) {
                    showLocationActivity()
                }
            }
        }
    }

    private fun openDialog(callback: CardContainer.OnLikeListener, shopUrl: String) {
        MaterialDialog.Builder(context!!)
            .title(getString(R.string.dialog_title))
            .content(getString(R.string.dialog_content))
            .positiveText(getString(R.string.dialog_positive_text))
            .negativeText(getString(R.string.dialog_negative_text))
            .onPositive(MaterialDialog.SingleButtonCallback { dialog, which ->
                Log.i("openDialog", "I choose positive.")
                callback.choose()
                cardCount--
                pickupCountTextView.text = getString(R.string.pickup_count, cardCount, HotPepperUtil.pickupCount)
                if (cardCount == 0) {
                    showLocationActivity()
                }
                val uri = Uri.parse(shopUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            })
            .onNegative(MaterialDialog.SingleButtonCallback { dialog, which ->
                Log.i("openDialog", "I choose negative.")
                callback.unchoose()
            })
            .cancelListener(DialogInterface.OnCancelListener {
                Log.i("openDialog", "cancel")
                callback.unchoose()
            })
            .show()
    }

    private fun showLocationActivity() {
        val intent = Intent(context, LocationActivity::class.java)
        startActivity(intent)
    }

    companion object {

        fun newInstance(): ShopSwipeFragment {
            return ShopSwipeFragment()
        }
    }
}
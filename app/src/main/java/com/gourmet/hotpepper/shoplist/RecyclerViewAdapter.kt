package com.gourmet.hotpepper.shoplist

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gourmet.hotpepper.R

class RecyclerViewAdapter(
    protected var dataSet: MutableList<ShopInfo>,
    private val listener: ShopListListener.ShopListClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shopNameTextView: TextView
        val shopPhotoImageView: ImageView
        val shopCatchTextView: TextView
        val shopBudgetTextView: TextView

        init {
            shopNameTextView = view.findViewById(R.id.shop_name_text_view)
            shopPhotoImageView = view.findViewById(R.id.shop_photo_image_view)
            shopCatchTextView = view.findViewById(R.id.shop_catch_text_view)
            shopBudgetTextView = view.findViewById(R.id.shop_budget_text_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.shop_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopInfo = dataSet[position]
        holder.shopNameTextView.text = shopInfo.getShopName()
        val imageTask = ImageGetTask()
        imageTask.setListener(createListener(holder.shopPhotoImageView))
        imageTask.execute(shopInfo.getPhoto())

        holder.shopCatchTextView.text = shopInfo.getShopCatch()
        holder.shopBudgetTextView.text = shopInfo.getShopBudget()

        // クリック処理
        holder.itemView.setOnClickListener {

            listener.onClickRow(shopInfo.getShopUrl())
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun createListener(shopPhotoImageView : ImageView) : ImageGetTaskListener {
        return object: ImageGetTaskListener {

            override fun onSuccess(result: Bitmap?) {

                // 取得した画像をImageViewに設定します。
                shopPhotoImageView.setImageBitmap(result)
            }
        }
    }
}
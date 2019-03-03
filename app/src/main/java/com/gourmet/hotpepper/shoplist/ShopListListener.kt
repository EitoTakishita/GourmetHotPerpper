package com.gourmet.hotpepper.shoplist

interface ShopListListener {

    interface ShopListClickListener {

        fun onClickRow(shopUrl: String?)
    }
}


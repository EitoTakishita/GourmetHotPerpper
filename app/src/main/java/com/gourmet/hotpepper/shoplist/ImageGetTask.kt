package com.gourmet.hotpepper.shoplist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


class ImageGetTask : AsyncTask<String, Void, Bitmap>() {

    private var imageGetTaskListener : ImageGetTaskListener? = null

    override fun doInBackground(vararg params: String): Bitmap? {

        val bitmap: Bitmap
        try {

            val imageUrl = URL(params[0])
            val imageIs: InputStream
            imageIs = imageUrl.openStream()
            bitmap = BitmapFactory.decodeStream(imageIs)
            return bitmap
        } catch (e: MalformedURLException) {

            return null
        } catch (e: IOException) {

            return null
        }

    }

    override fun onPostExecute(result: Bitmap?) {

        if (imageGetTaskListener != null) {

            imageGetTaskListener!!.onSuccess(result)
        }
    }

    fun setListener(listener: ImageGetTaskListener) {

        imageGetTaskListener = listener
    }
}
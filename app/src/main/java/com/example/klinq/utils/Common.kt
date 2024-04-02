package com.example.klinq.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.klinq.R
import com.bumptech.glide.load.engine.Resource
import com.google.android.material.snackbar.Snackbar

class Common {
    companion object{
        fun loadImage(imgView: ImageView, url:String){
            Glide.with(imgView).load(url).placeholder(R.drawable.loading).into(imgView)
        }
        fun showFixSnackbar(rootView: View, activity: Activity, message: String){
            Snackbar
                .make(rootView, message, Snackbar.LENGTH_INDEFINITE)

                .setAction("TryAgain") {
                    val intent: Intent = activity.intent
                    activity.finish()
                    activity.startActivity(intent)
                }
                .setBackgroundTint(Color.RED)
                .setActionTextColor(Color.DKGRAY)
                .show()
        }
        fun showSnackbar(rootView: View, activity: Activity, message: String){
            Snackbar
                .make(rootView, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.RED)
                .setActionTextColor(Color.DKGRAY)
                .show()
        }
    }
}
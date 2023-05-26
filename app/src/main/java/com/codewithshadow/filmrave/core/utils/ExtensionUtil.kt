package com.codewithshadow.filmrave.core.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.codewithshadow.filmrave.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ImageView.loadImage(
    url: Any?,
    placeholder: Drawable? = null,
    listener: RequestListener<Drawable>? = null,
    skipCache: Boolean = true,
) {
    try {
        Glide.with(this.context)
            .load(url)
            .timeout(180000)
            .placeholder(placeholder)
            .error(R.drawable.poster_bg)
            .addListener(listener)
            .skipMemoryCache(skipCache)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(this)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}

fun TextView.formatMediaDate(inputTime: String?) = if (!inputTime.isNullOrEmpty()) {
    // Making SDF object by giving input time patter
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // inputPatter: yyyy-MM-dd
    // Parsing inputTime
    val parsedDate: Date? = sdf.parse(inputTime)
    // Formatting parsed input time/date
    val formattedTime = SimpleDateFormat("yyyy", Locale.getDefault()).format(parsedDate)
    // Setting time to this textview
    this.text = formattedTime
} else text = "Unknown"


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

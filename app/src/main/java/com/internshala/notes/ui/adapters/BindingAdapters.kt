package com.internshala.notes.ui.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("isVisible")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("imgUrl", "error")
fun loadImage(view: ImageView, url: String, error: Drawable) {
    Glide.with(view)
        .load(url)
        .error(error)
        .into(view)
}
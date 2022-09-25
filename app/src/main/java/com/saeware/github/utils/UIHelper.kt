package com.saeware.github.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate.*
import com.bumptech.glide.Glide
import com.saeware.github.R
import de.hdodenhof.circleimageview.CircleImageView

class UIHelper {
    companion object {
        fun CircleImageView.setImage(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.avatar)
                .into(this)
        }

        fun ProgressBar.setVisible(state: Boolean) {
            this.visibility = if (state) View.VISIBLE else View.GONE
        }

        fun TextView.setTextAndVisible(text: String?) {
            if (!text.isNullOrEmpty()) {
                this.text = text
                this.visibility = View.VISIBLE
            }
        }

        fun toggleDarkMode(state: Boolean) {
            setDefaultNightMode(if (state) MODE_NIGHT_YES else MODE_NIGHT_NO)
        }
    }
}
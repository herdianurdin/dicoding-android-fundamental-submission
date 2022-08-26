package com.saeware.github.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saeware.github.R
import com.saeware.github.databinding.ActivityDetailUserBinding
import com.saeware.github.model.User

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        parseUserData(user)

        binding.btnOpen.setOnClickListener {
            Intent(Intent.ACTION_VIEW)
                .apply { data = Uri.parse("https://www.github.com/${user.username}") }
                .also { startActivity(it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun parseUserData(user: User) {
        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(user.avatar)
                .apply(RequestOptions().override(72, 72))
                .into(ivAvatar)

            tvName.text = user.name
            tvUsername.text = user.username
            tvRepository.text = abbreviationNum(user.repository)
            tvFollowers.text = abbreviationNum(user.followers)
            tvFollowing.text = abbreviationNum(user.following)
            tvCompany.text = user.company
            tvLocation.text = user.location
        }
    }

    private fun abbreviationNum(num: Int): String {
        return when (num) {
            in 0..999 -> num.toString()
            in 1000..999999 -> "${num /1000}K"
            in 1000000..9999999 -> "${num / 1000000}M"
            else -> "${num / 1000000000}B"
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}
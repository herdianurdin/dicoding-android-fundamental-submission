package com.saeware.github.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saeware.github.R
import com.saeware.github.adapter.SectionsPagerAdapter
import com.saeware.github.databinding.ActivityDetailUserBinding
import com.saeware.github.model.DetailUser
import com.saeware.github.utils.ViewAttribute.Companion.setImage
import com.saeware.github.utils.ViewAttribute.Companion.setTextAndVisible
import com.saeware.github.utils.ViewAttribute.Companion.setVisible

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var username: String
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.extras?.get(EXTRA_USERNAME) as String

        detailViewModel.loading.observe(this) {
            showLoading(it)
        }

        detailViewModel.error.observe(this) {
            showErrorOccurred(it)
        }

        detailViewModel.hasLoaded.observe(this) {
            if (!it) detailViewModel.getUserDetail(username)
        }

        detailViewModel.user.observe(this) {
            if (it != null) parseUserData(it)
        }

        setViewPager()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(loading: Boolean) {
        binding.apply {
            progressBar.setVisible(loading)
            appBarLayout.visibility = if (loading) View.GONE else View.VISIBLE
            nestedScrollView.visibility = if(loading) View.GONE else View.VISIBLE
        }
    }

    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionsPagerAdapter(this, username)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun parseUserData(user: DetailUser) {
        binding.apply {
            tvUsername.text = user.login
            tvRepository.text = abbreviationNum(user.publicRepos)
            tvFollowers.text = abbreviationNum(user.followers)
            tvFollowing.text = abbreviationNum(user.following)

            tvName.setTextAndVisible(user.name)
            tvBio.setTextAndVisible(user.bio)
            tvCompany.setTextAndVisible(user.company)
            tvLocation.setTextAndVisible(user.location)
            tvBlog.setTextAndVisible(user.blog)

            ivAvatar.setImage(this@DetailUserActivity, user.avatarUrl)

            btnOpen.setOnClickListener {
                Intent(Intent.ACTION_VIEW)
                    .apply { data = Uri.parse(user.htmlUrl) }
                    .also { startActivity(it) }
            }
        }
    }

    private fun abbreviationNum(num: Int): String {
        return when (num) {
            in 0..999 -> num.toString()
            in 1_000..999999 -> "${num / 1_000}K"
            in 1_000_000..9999999 -> "${num / 1_000_000}M"
            else -> "${num / 1000_000_000}B"
        }
    }

    private fun showErrorOccurred(error: Boolean) {
        if (error) {
            binding.apply {
                appBarLayout.visibility = View.GONE
                nestedScrollView.visibility = View.GONE
                tvErrorResult.visibility = View.VISIBLE
            }

            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.error_occurred),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}
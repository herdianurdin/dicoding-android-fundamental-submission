package com.saeware.github.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saeware.github.R
import com.saeware.github.adapter.SectionsPagerAdapter
import com.saeware.github.data.remote.response.DetailUser
import com.saeware.github.databinding.ActivityDetailUserBinding
import com.saeware.github.utils.UIHelper.Companion.setImage
import com.saeware.github.utils.UIHelper.Companion.setTextAndVisible
import com.saeware.github.utils.UIHelper.Companion.setVisible
import com.saeware.github.utils.ViewModelFactory
import com.saeware.github.data.Result
import com.saeware.github.data.local.entity.UserEntity
import com.saeware.github.utils.ConverterHelper.abbreviationNum

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var username: String
    private lateinit var user: DetailUser
    private var isFavorite: Boolean = false
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getFactory(this)
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback (true) {
            override fun handleOnBackPressed() { finish() }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.getStringExtra(EXTRA_USERNAME) as String

        detailViewModel.hasLoaded.observe(this) {
            if (!it) detailViewModel.getUserDetail(username)
            detailViewModel.isFavoriteUser(username)
        }

        detailViewModel.user.observe(this) { handleReceivedData(it) }
        detailViewModel.isFavoriteUser.observe(this) { isFavoriteUser(it) }

        setViewPager()

        binding.fabFavorite.setOnClickListener { toggleFavoriteUser() }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun handleReceivedData(result: Result<DetailUser>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                showLoading(false)
                showErrorOccurred()
            }
            is Result.Success -> {
                showLoading(false)
                user = result.data
                parseUserData(result.data)
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.apply {
            progressBar.setVisible(loading)
            appBarLayout.visibility = if (loading) View.GONE else View.VISIBLE
            nestedScrollView.visibility = if(loading) View.GONE else View.VISIBLE
            fabFavorite.visibility = if (loading) View.GONE else View.VISIBLE
        }
    }

    private fun showErrorOccurred() {
        binding.apply {
            appBarLayout.visibility = View.GONE
            nestedScrollView.visibility = View.GONE
            fabFavorite.visibility = View.GONE
            tvErrorResult.visibility = View.VISIBLE
        }

        detailViewModel.showErrorOccurred {
            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.error_occurred),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isFavoriteUser(state: Boolean) {
        isFavorite = state
        binding.fabFavorite.setImageResource(
            if (state) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
    }

    private fun toggleFavoriteUser() {
        val userEntity = UserEntity(user.login, user.avatarUrl, !isFavorite)

        if (isFavorite) {
            detailViewModel.removeUserFromFavorite(userEntity)
            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.remove_as_favorite),
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            detailViewModel.addUserAsFavorite(userEntity)
            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.add_as_favorite),
                Toast.LENGTH_SHORT
            ).show()
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
            tvRepository.text = abbreviationNum(user.publicRepos.toLong())
            tvFollowers.text = abbreviationNum(user.followers.toLong())
            tvFollowing.text = abbreviationNum(user.following.toLong())

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

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}
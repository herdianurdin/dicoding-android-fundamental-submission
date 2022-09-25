package com.saeware.github.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saeware.github.R
import com.saeware.github.adapter.UserAdapter
import com.saeware.github.data.local.entity.UserEntity
import com.saeware.github.data.remote.response.User
import com.saeware.github.databinding.ActivityFavoriteBinding
import com.saeware.github.ui.activity.DetailUserActivity.Companion.EXTRA_USERNAME
import com.saeware.github.utils.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getFactory(this)
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteViewModel.favoriteUsers.observe(this) { handleReceivedData(it) }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun handleReceivedData(favoriteUsers: List<UserEntity>) {
        if (favoriteUsers.isEmpty()) {
            binding.rvUser.visibility = View.GONE
            binding.tvStatus.visibility = View.VISIBLE
        } else showFavoriteUsers(favoriteUsers)
    }

    private fun showFavoriteUsers(favoriteUsers: List<UserEntity>) {
        val users = ArrayList<User>()

        favoriteUsers.forEach {
            val user = User(it.avatarUrl, it.id)
            users.add(user)
        }

        val userAdapter = UserAdapter(users)

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }

        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(user: User) { showDetailUser(user) }
        })
    }

    private fun showDetailUser(user: User) {
        Intent(this, DetailUserActivity::class.java)
            .apply { putExtra(EXTRA_USERNAME, user.login) }
            .also { startActivity(it) }
    }
}
package com.saeware.github.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saeware.github.R
import com.saeware.github.adapter.UserAdapter
import com.saeware.github.databinding.ActivityMainBinding
import com.saeware.github.model.User
import com.saeware.github.ui.activity.DetailUserActivity.Companion.EXTRA_USERNAME
import com.saeware.github.utils.ViewAttribute.Companion.setVisible

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.loading.observe(this) { binding.progressBar.setVisible(it) }
        mainViewModel.error.observe(this) { showErrorOccurred(it) }
        mainViewModel.users.observe(this) { showSearchResult(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu.findItem(R.id.search_action)?.actionView as SearchView
        searchView.apply {
            maxWidth = Int.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.searchUserByUsername(query ?: "")
                    clearFocus()

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }

        return true
    }

    private fun showSearchResult(users: ArrayList<User>) {
        val userAdapter = UserAdapter(users)

        binding.tvResultCount.text = getString(R.string.showing_results, users.size)
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }

        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(user: User) {
                showDetailUser(user)
            }
        })
    }

    private fun showDetailUser(user: User) {
        Intent(this, DetailUserActivity::class.java)
            .apply { putExtra(EXTRA_USERNAME, user.login) }
            .also { startActivity(it) }
    }

    private fun showErrorOccurred(error: Boolean) {
        binding.apply {
            tvResultCount.visibility = if (error) View.GONE else View.VISIBLE
            rvUser.visibility = if (error) View.GONE else View.VISIBLE
            tvErrorResult.visibility = if (error) View.VISIBLE else View.GONE
        }

        if (error) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.error_occurred),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
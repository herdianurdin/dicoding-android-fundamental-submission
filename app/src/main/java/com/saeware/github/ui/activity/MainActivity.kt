package com.saeware.github.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saeware.github.R
import com.saeware.github.adapter.UserAdapter
import com.saeware.github.data.Result
import com.saeware.github.data.remote.response.User
import com.saeware.github.databinding.ActivityMainBinding
import com.saeware.github.ui.activity.DetailUserActivity.Companion.EXTRA_USERNAME
import com.saeware.github.utils.UIHelper.Companion.setVisible
import com.saeware.github.utils.UIHelper.Companion.toggleDarkMode
import com.saeware.github.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getFactory(this)
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getDarkModeSetting().observe(this) { toggleDarkMode(it) }
        mainViewModel.users.observe(this) { handleReceivedData(it) }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_action ->
                Intent(this, FavoriteActivity::class.java)
                    .also { startActivity(it) }
            R.id.settings_action ->
                Intent(this, SettingsActivity::class.java)
                    .also { startActivity(it) }
        }

        return super.onOptionsItemSelected(item)
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

    private fun handleReceivedData(result: Result<ArrayList<User>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                showLoading(false)
                showErrorOccurred(true)
            }
            is Result.Success -> {
                showLoading(false)
                showErrorOccurred(false)
                showSearchResult(result.data)
            }
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.setVisible(state) }

    private fun showErrorOccurred(error: Boolean) {
        binding.apply {
            tvResultCount.visibility = if (error) View.GONE else View.VISIBLE
            rvUser.visibility = if (error) View.GONE else View.VISIBLE
            tvErrorResult.visibility = if (error) View.VISIBLE else View.GONE
        }

        if (error) {
            mainViewModel.showErrorOccurred {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_occurred),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
}
package com.saeware.github.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saeware.github.R
import com.saeware.github.adapter.UserListAdapter
import com.saeware.github.databinding.ActivityMainBinding
import com.saeware.github.model.User
import com.saeware.github.ui.DetailUserActivity.Companion.EXTRA_USER

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUser: RecyclerView
    private lateinit var userListAdapter: UserListAdapter
    private val list = ArrayList<User>()
    private val listAll = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvUser
        rvUser.setHasFixedSize(true)

        list.addAll(userList)
        listAll.addAll(userList)

        showUserList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu.findItem(R.id.search_action)?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String?): Boolean = false

            override fun onQueryTextChange(keyword: String?): Boolean = handleSearch(keyword)
        })

        return true
    }

    private fun handleSearch(keyword: String?): Boolean {
        if (!keyword.isNullOrEmpty()) {
            val users = listAll.filter { it.name.lowercase().contains(keyword.lowercase()) }
            val newUserList = ArrayList<User>()

            newUserList.addAll(users)
            userListAdapter.setUserList(newUserList)
        } else userListAdapter.setUserList(listAll)

        return true
    }

    private val userList: ArrayList<User>
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)

            val userList = ArrayList<User>()
            for (i in dataUsername.indices) {
                val user = User(
                    dataUsername[i],
                    dataName[i],
                    dataLocation[i],
                    dataRepository[i].toInt(),
                    dataCompany[i],
                    dataFollowers[i].toInt(),
                    dataFollowing[i].toInt(),
                    dataAvatar.getResourceId(i, -1)
                )
                userList.add(user)
            }
            dataAvatar.recycle()

            return userList
        }

    private fun showUserList() {
        userListAdapter = UserListAdapter(list)

        rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                GridLayoutManager(this, 2)
            else LinearLayoutManager(this)
        rvUser.adapter = userListAdapter

        userListAdapter.setOnItemClickCallback(object: UserListAdapter.OnItemClickCallBack {
            override fun onItemClicked(user: User) {
                showDetailUser(user)
            }
        })
    }

    private fun showDetailUser(user: User) {
        Intent(this, DetailUserActivity::class.java)
            .apply { putExtra(EXTRA_USER, user) }
            .also { startActivity(it) }
    }
}
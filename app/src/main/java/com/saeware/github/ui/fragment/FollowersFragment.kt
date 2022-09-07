package com.saeware.github.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saeware.github.adapter.SectionsPagerAdapter.Companion.ARGS_USERNAME
import com.saeware.github.adapter.UserAdapter
import com.saeware.github.databinding.FragmentFollowersBinding
import com.saeware.github.model.User
import com.saeware.github.ui.activity.DetailUserActivity
import com.saeware.github.utils.ViewAttribute.Companion.setVisible

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val followersViewModel: FollowersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)

        followersViewModel.followers.observe(viewLifecycleOwner) { followers ->
            if (followers == null) {
                val username = arguments?.getString(ARGS_USERNAME) ?: ""
                followersViewModel.getUserFollowers(username)
            } else showFollowers(followers)
        }

        followersViewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.setVisible(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showFollowers(users: ArrayList<User>) {
        if (users.size > 0) {
            val userAdapter = UserAdapter(users)

            binding.rvUser.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = userAdapter
                setHasFixedSize(true)
            }

            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack {
                override fun onItemClicked(user: User) {
                    showDetailUser(user)
                }
            })
        } else binding.tvStatus.visibility = View.VISIBLE
    }

    private fun showDetailUser(user: User) {
        Intent(activity, DetailUserActivity::class.java)
            .apply { putExtra(DetailUserActivity.EXTRA_USERNAME, user.login) }
            .also { startActivity(it) }
    }
}
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
import com.saeware.github.databinding.FragmentFollowingBinding
import com.saeware.github.ui.activity.DetailUserActivity
import com.saeware.github.utils.ViewModelFactory
import com.saeware.github.data.Result
import com.saeware.github.data.remote.response.User
import com.saeware.github.utils.UIHelper.Companion.setVisible

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val followingViewModel: FollowingViewModel by viewModels {
        ViewModelFactory.getFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARGS_USERNAME) ?: ""

        followingViewModel.hasLoaded.observe(viewLifecycleOwner) {
            if (!it) followingViewModel.getUserFollowing(username)
        }

        followingViewModel.following.observe(viewLifecycleOwner) { handleReceivedData(it) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun handleReceivedData(result: Result<ArrayList<User>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> showLoading(false)
            is Result.Success -> {
                showLoading(false)
                showFollowing(result.data)
            }
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.setVisible(state) }

    private fun showFollowing(users: ArrayList<User>) {
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
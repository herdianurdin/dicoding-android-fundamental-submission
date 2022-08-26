package com.saeware.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saeware.github.databinding.UserCardBinding
import com.saeware.github.model.User

class UserListAdapter(private var userList: ArrayList<User>): RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    class ListViewHolder(var binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = userList[position]

        holder.binding.apply {
            tvName.text = user.name
            tvUsername.text = user.username
            Glide.with(holder.itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(56, 56))
                .into(ivAvatar)
        }
        holder.itemView.setOnClickListener { onItemClickCallBack.onItemClicked(user) }
    }

    override fun getItemCount(): Int = userList.size

    fun setUserList(newUserList: ArrayList<User>) {
        val diffUtil = UserListDiffUtil(userList, newUserList)
        val diffUtilResult = DiffUtil.calculateDiff((diffUtil))
        userList = newUserList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(user: User)
    }
}
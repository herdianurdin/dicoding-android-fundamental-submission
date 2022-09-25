package com.saeware.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saeware.github.data.remote.response.User
import com.saeware.github.databinding.UserCardBinding
import com.saeware.github.utils.UIHelper.Companion.setImage

class UserAdapter(private val users: ArrayList<User>)
    : RecyclerView.Adapter<UserAdapter.ListViewHolder>()
{
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    class ListViewHolder(var binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = users[position]

        holder.binding.apply {
            tvUsername.text = user.login
            ivAvatar.setImage(holder.itemView.context, user.avatarUrl)
        }
        holder.itemView.setOnClickListener { onItemClickCallBack.onItemClicked(user) }
    }

    override fun getItemCount(): Int = users.size

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(user: User)
    }
}
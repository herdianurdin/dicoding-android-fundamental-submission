package com.saeware.github.adapter

import androidx.recyclerview.widget.DiffUtil
import com.saeware.github.model.User

class UserListDiffUtil(
    private val oldList: ArrayList<User>,
    private val newList: ArrayList<User>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldPosition: Int,
        newPosition: Int
    ): Boolean = oldList[oldPosition].username == newList[newPosition].username

    override fun areContentsTheSame(
        oldPosition: Int,
        newPosition: Int
    ): Boolean = oldList[oldPosition] == newList[newPosition]
}
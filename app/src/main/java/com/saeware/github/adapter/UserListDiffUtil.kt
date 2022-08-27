package com.saeware.github.adapter

import androidx.recyclerview.widget.DiffUtil
import com.saeware.github.model.User

class UserListDiffUtil(
    private val oldUserList: ArrayList<User>,
    private val newUserList: ArrayList<User>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldUserList.size

    override fun getNewListSize(): Int = newUserList.size

    override fun areItemsTheSame(
        oldPosition: Int,
        newPosition: Int
    ): Boolean = oldUserList[oldPosition].username == newUserList[newPosition].username

    override fun areContentsTheSame(
        oldPosition: Int,
        newPosition: Int
    ): Boolean = oldUserList[oldPosition] == newUserList[newPosition]
}
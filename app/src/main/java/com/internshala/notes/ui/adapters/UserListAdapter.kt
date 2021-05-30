package com.internshala.notes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.internshala.notes.R
import com.internshala.notes.data.models.User
import com.internshala.notes.databinding.ItemUserBinding

class UserListAdapter : ListAdapter<User, UserListAdapter.UserViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        return holder.bind(item)
    }

    class UserViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        private val _binding: ItemUserBinding = binding

        fun bind(user: User) {
            _binding.user = user
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val inflater = LayoutInflater.from(parent.context)

                val binding = DataBindingUtil.inflate<ItemUserBinding>(
                    inflater,
                    R.layout.item_user,
                    parent,
                    false
                )
                return UserViewHolder(binding)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return true // Contents of an item will always be same
        }
    }
}
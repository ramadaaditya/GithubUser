package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.databinding.ItemUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.MyViewHolder>() {
    private var onItemClickCallback: ((Item) -> Unit)? = null
    private var listUser = listOf<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
    fun setUserList(newList: List<Item>) {
        val diffCallback = UserDiffCallback(listUser, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listUser = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Item) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(image)
                binding.tvUsername.text = user.login
                itemView.setOnClickListener {
                    onItemClickCallback?.invoke(user)
                }
            }
        }
    }

    fun setOnItemClickCallback(callback: (Item) -> Unit) {
        onItemClickCallback = callback
    }

    class UserDiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem].id == newList[newItem].id
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem] == newList[newItem]
        }
    }
}


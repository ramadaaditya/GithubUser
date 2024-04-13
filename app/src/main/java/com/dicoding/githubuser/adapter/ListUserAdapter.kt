package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ItemUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.MyViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private var listUser = ArrayList<ItemsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun setUserList(newList: List<ItemsItem>) {
        val diffResult = DiffUtil.calculateDiff(DIFFCALLBACK(listUser, newList))
        listUser.clear()
        listUser.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class DIFFCALLBACK(
        private val oldList: List<ItemsItem>,
        private val newList: List<ItemsItem>
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


    inner class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(image)
                binding.tvUsername.text = user.login
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }
    


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}


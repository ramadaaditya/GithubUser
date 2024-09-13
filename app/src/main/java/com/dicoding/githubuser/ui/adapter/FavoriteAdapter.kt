package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ItemUserBinding

class FavoriteAdapter(private var items: List<UserEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var onItemClickCallback: ((UserEntity) -> Unit)? = null
    fun setData(newItems: List<UserEntity>) {
        val diffCallback = FavoriteDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

    }

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(userEntity.avatarUrl)
                    .centerCrop()
                    .into(image)
                tvUsername.text = userEntity.username
                itemView.setOnClickListener {
                    onItemClickCallback?.invoke(userEntity)
                }
            }
        }
    }

    fun setOnItemClickCallback(callback: (UserEntity) -> Unit) {
        onItemClickCallback = callback
    }

    class FavoriteDiffCallback(
        private val oldItems: List<UserEntity>,
        private val newItems: List<UserEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].username == newItems[newItemPosition].username
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}
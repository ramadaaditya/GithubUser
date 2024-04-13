package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.databinding.ItemUserBinding

class FavoriteAdapter(private val listFavorite: List<UserEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : UserEntity){
            with(binding){
                tvUsername.text = user.username
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(image)
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavorite[position])

    }

}
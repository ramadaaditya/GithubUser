package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite User"


        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorite().observe(this) {
            setFavoriteData(it)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setFavoriteData(userEntities: List<UserEntity>) {
        val items = arrayListOf<UserEntity>()
        userEntities.map {
            val item = UserEntity(
                username = it.username,
                avatarUrl = it.avatarUrl,
            )
            items.add(item)
        }

        val adapter = FavoriteAdapter(items)
        with(binding){
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = adapter
        }

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                startActivity(
                    Intent(this@FavoriteActivity,DetailActivity::class.java)
                        .putExtra(DetailActivity.EXTRA_USER, data.username)
                        .putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }


    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}
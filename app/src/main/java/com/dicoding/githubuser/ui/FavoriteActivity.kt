package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupRecyclerView()
        setupObservers()

    }

    private fun setupObservers() {
        favoriteViewModel.getAllFavorite().observe(this) { userEntities ->
            setFavoriteData(userEntities)
        }

        favoriteViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(emptyList())
        adapter.setOnItemClickCallback { data ->
            startActivity(
                Intent(this@FavoriteActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_USER, data.username)
                    .putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            )
        }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = this@FavoriteActivity.adapter
        }
    }

    private fun setupViewModel() {
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.favorite_title)
        }
    }

    private fun setFavoriteData(userEntities: List<UserEntity>) {
        adapter.setData(userEntities)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
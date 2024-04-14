package com.dicoding.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.ui.ViewModelFactory
import com.dicoding.githubuser.ui.adapter.FavoriteAdapter
import com.dicoding.githubuser.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()
        setupObservers()

        favoriteViewModel.getAllFavorite()
    }

    private fun setupObservers() {
        favoriteViewModel.favorite.observe(this) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    setFavoriteData(resultState.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, resultState.error, Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
package com.dicoding.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favorite.databinding.FavoriteUserItemBinding
import com.dicoding.githubuser.R
import com.dicoding.githubuser.ui.ListUserAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: FavoriteUserItemBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoriteUserItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadKoinModules(FavModule)
        supportActionBar?.title = getString(R.string.favorite_title)
        val layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.rvFavorite.layoutManager = layoutManager
        setupFavorite()
    }

    private fun setupFavorite() {
        favoriteViewModel.getAllFavorite().observe(this) {
            it?.let {
                binding.rvFavorite.apply {
                    val adapter = ListUserAdapter()
                    adapter.submitList(it)
                    binding.rvFavorite.adapter = adapter
                    DividerItemDecoration(this@FavoriteActivity, DividerItemDecoration.VERTICAL)
                }
            }
        }
    }
}
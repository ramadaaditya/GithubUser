package com.dicoding.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.data.source.Resource
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.ListUserAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private val adapter = ListUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupObserver()
        setupSearchView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUserGithub.apply {
            this.layoutManager = layoutManager
            this.adapter = this@HomeActivity.adapter
            addItemDecoration(DividerItemDecoration(this@HomeActivity, layoutManager.orientation))
        }
    }

    private fun setupObserver() {
        homeViewModel.username.value = "Ramada"
        homeViewModel.user.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    adapter.submitList(result.data)
                    showLoading(false)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()

                    searchData(searchBar.text.toString())
                    false
                }
            searchBar.apply {
                inflateMenu(R.menu.search)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.favorite -> {
                            navigateToFavoriteActivity()
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun searchData(query: String) {
        homeViewModel.username.value = query

    }

    private fun navigateToFavoriteActivity() {
        try {
            startActivity(Intent(this, Class.forName("com.dicoding.favorite.FavoriteActivity")))
        } catch (e: Exception) {
            Toast.makeText(this, "Favorite module not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
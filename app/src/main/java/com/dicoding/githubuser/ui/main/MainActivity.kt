package com.dicoding.githubuser.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.ViewModelFactory
import com.dicoding.githubuser.ui.adapter.ListUserAdapter
import com.dicoding.githubuser.ui.detail.DetailActivity
import com.dicoding.githubuser.ui.favorite.FavoriteActivity
import com.dicoding.githubuser.ui.setting.DarkTheme
import com.dicoding.githubuser.ui.setting.SettingPreferences
import com.dicoding.githubuser.ui.setting.SettingViewModel
import com.dicoding.githubuser.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val settingViewModel: SettingViewModel by viewModels<SettingViewModel> {
        SettingViewModel.ViewModelFactory(SettingPreferences.getInstance(dataStore))
    }
    private val mainViewModel: MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.user.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    adapter.setUserList(result.data)
                    Log.d(TAG, "Cek isi data : ${result.data} ")
                    showLoading(false)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            setupSearchView()
            setupRecyclerView()
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = binding.searchView.text.toString()
                mainViewModel.searchUsers(query)
                searchBar.setText(searchView.text)
                binding.searchView.hide()
                true
            }
            setupMenu()
        }
    }

    private fun setupMenu() {
        binding.searchBar.apply {
            inflateMenu(R.menu.search)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        navigateToFavoriteActivity()
                        true
                    }

                    R.id.theme -> {
                        navigateToDarkThemeActivity()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun navigateToFavoriteActivity() {
        startActivity(Intent(this, FavoriteActivity::class.java))
    }

    private fun navigateToDarkThemeActivity() {
        startActivity(Intent(this, DarkTheme::class.java))
    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUserGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserGithub.addItemDecoration(itemDecoration)

        adapter = ListUserAdapter()
        adapter.setOnItemClickCallback(::navigateToDetailActivity)
        binding.rvUserGithub.adapter = adapter
    }

    private fun navigateToDetailActivity(data: Item) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, data.login)
            putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.searchBar.menu.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_full)
            binding.searchBar.menu.findItem(R.id.theme)?.setIcon(R.drawable.ic_light_white)
        } else {
            binding.searchBar.menu.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_yellow)
            binding.searchBar.menu.findItem(R.id.theme)?.setIcon(R.drawable.ic_light_yellow)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
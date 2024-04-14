package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.settings.DarkTheme
import com.dicoding.githubuser.settings.SettingPreferences
import com.dicoding.githubuser.settings.dataStore
import com.dicoding.githubuser.viewModel.MainViewModel
import com.dicoding.githubuser.viewModel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModel.ViewModelFactory(SettingPreferences.getInstance(dataStore))
    }
    private val mainViewModel: MainViewModel by viewModels()
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
        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        mainViewModel.listUser.observe(this) { list ->
            adapter.setUserList(list)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
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
                mainViewModel.searchUser(query)
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
        adapter.setOnItemClickCallback { data ->
            navigateToDetailActivity(data)
        }
        binding.rvUserGithub.adapter = adapter
    }

    private fun navigateToDetailActivity(data: ItemsItem) {
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

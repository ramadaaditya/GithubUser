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
    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.ViewModelFactory(SettingPreferences.getInstance(dataStore))
    }
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
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

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserGithub.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = binding.searchView.text.toString()
                    mainViewModel.searchUser(query)
                    searchBar.setText(searchView.text)
                    binding.searchView.hide()
                    true
                }

            searchBar.inflateMenu(R.menu.search)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.theme -> {
                        val intent = Intent(this@MainActivity, DarkTheme::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        adapter = ListUserAdapter()
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvUserGithub.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUserGithub.setHasFixedSize(true)
            rvUserGithub.adapter = adapter
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
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

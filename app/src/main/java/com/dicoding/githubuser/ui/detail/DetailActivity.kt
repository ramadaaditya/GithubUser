package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.core.data.source.Resource
import com.dicoding.core.domain.model.User
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.ui.loadImage
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel: DetailViewModel by viewModel()
    private var isFavorite = false
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_title)
        val username = intent.getStringExtra("username").toString()
        setupViewPager()
        setupView(username)
        setupFavorite()
    }


    private fun setupView(username: String) {
        detailViewModel.getDetailUser(username).observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.let { userData ->
                        with(binding) {
                            userpicture.loadImage(userData.avatarUrl)
                            tvUsername.text = userData.login
                            tvfullname.text = userData.name
                            follower.text = getString(R.string.follower, userData.followers)
                            following.text = getString(R.string.follow, userData.following)
                            user = userData
                        }
                    }
                    checkFavorite(username)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun checkFavorite(username: String) {
        detailViewModel.getDataByUsername(username)?.observe(this) { userData ->
            if (userData.isFavorite != null) {
                isFavorite = true
                binding.fab.setImageResource(R.drawable.ic_favorite)
            } else {
                isFavorite = false
                binding.fab.setImageResource(R.drawable.ic_unfavorite)
            }
        }
    }

    private fun setupFavorite() {
        binding.fab.setOnClickListener {
            if (isFavorite) {
                user.isFavorite = false
                detailViewModel.deleteDataUser(user)
                showToast(R.string.favorite_remove)
                binding.fab.setImageResource(R.drawable.ic_unfavorite)
            } else {
                user.isFavorite = true
                detailViewModel.insertDataUser(user)
                showToast(R.string.favorite_add)
                binding.fab.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
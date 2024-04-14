package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.remote.response.DetailResponse
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.ui.ViewModelFactory
import com.dicoding.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var sectionsPagerAdapter: SectionPagerAdapter
    private var isFavorite: Boolean = false
    private lateinit var username: String
    private lateinit var avatarUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USER) ?: ""
        avatarUrl = intent.getStringExtra(EXTRA_AVATAR) ?: ""

        setupActionBar()
        setupViewPager()
        setupFavoriteButton()
        setupObservers()

        detailViewModel.getDetailUser(username)
        showLoading(true)
    }

    private fun setupObservers() {
        detailViewModel.user.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    handleSuccess(state.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
            }

            detailViewModel.getDataByUsername(username).observe(this) { favoriteUser ->
                updateFavoriteStatus(favoriteUser.isNotEmpty())
            }
        }
    }

    private fun setupFavoriteButton() {
        binding.fab.setOnClickListener {
            val favoriteUser = UserEntity(username, avatarUrl)
            if (isFavorite) {
                detailViewModel.deleteDataUser(favoriteUser)
                showToast(R.string.favorite_remove)
            } else {
                detailViewModel.insertDataUser(favoriteUser)
                showToast(R.string.favorite_add)
            }
        }
    }

    private fun setupViewPager() {
        sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detailUser)
            elevation = 0F
        }
    }

    private fun handleSuccess(detailResponse: DetailResponse) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(avatarUrl)
                .centerCrop()
                .into(userpicture)
            tvUsername.text = detailResponse.login
            tvfullname.text = detailResponse.name
            follower.text =
                resources.getString(R.string.follower, detailResponse.followers)
            following.text =
                resources.getString(R.string.follow, detailResponse.following)
        }
    }

    private fun updateFavoriteStatus(isFavoriteUser: Boolean) {
        isFavorite = isFavoriteUser
        val fabDrawable =
            if (isFavorite) R.drawable.ic_favorite_yellow else R.drawable.ic_favorite_border
        val fabContentDesc = if (isFavorite) R.string.favorite_remove else R.string.favorite_add

        binding.fab.apply {
            setImageDrawable(ContextCompat.getDrawable(context, fabDrawable))
            contentDescription = getString(fabContentDesc)
        }
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

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
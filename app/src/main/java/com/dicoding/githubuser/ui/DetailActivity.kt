package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionPagerAdapter
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    private lateinit var sectionsPagerAdapter: SectionPagerAdapter
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewPager()
        setupObservers()

        val username = intent.getStringExtra(EXTRA_USER) ?: ""
        detailViewModel.getDetailUser(username)
        showLoading(true)
    }

    private fun setupObservers() {
        detailViewModel.detailUser.observe(this) { user ->
            user?.let {
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(intent.getStringExtra(EXTRA_AVATAR))
                        .centerCrop()
                        .into(userpicture)
                    tvUsername.text = it.login
                    tvfullname.text = it.name
                    follower.text = resources.getString(R.string.follower, it.followers)
                    following.text = resources.getString(R.string.follow, it.following)
                }
            }
        }

        val username = intent.getStringExtra(EXTRA_USER) ?: ""

        detailViewModel.getDataByUsername(username).observe(this) { favoriteUsers ->
            isFavorite = favoriteUsers.isNotEmpty()
            val fabDrawable =
                if (isFavorite) R.drawable.ic_favorite_yellow else R.drawable.ic_favorite_border
            val fabContentDesc = if (isFavorite) R.string.favorite_remove else R.string.favorite_add

            binding.fab.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fab.context,
                    fabDrawable
                )
            )
            binding.fab.contentDescription = getString(fabContentDesc)

            binding.fab.setOnClickListener {
                val favoriteUser = UserEntity(username, intent.getStringExtra(EXTRA_AVATAR) ?: "")
                if (isFavorite) {
                    detailViewModel.deleteDataUser(favoriteUser)
                    Toast.makeText(this, fabContentDesc, Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insertDataUser(favoriteUser)
                    Toast.makeText(this, fabContentDesc, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupViewPager() {
        val username = intent.getStringExtra(EXTRA_USER) ?: ""
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
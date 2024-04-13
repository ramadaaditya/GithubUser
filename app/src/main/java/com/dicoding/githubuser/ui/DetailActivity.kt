package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionPagerAdapter
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER) ?: ""
        val avatar = intent.getStringExtra(EXTRA_AVATAR) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        supportActionBar?.title = getString(R.string.detailUser)

        supportActionBar?.elevation = 0F

        detailViewModel.getDetailUser(username)
        showLoading(true)

        detailViewModel.detailUser.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(avatar)
                        .centerCrop()
                        .into(userpicture)
                    tvUsername.text = it.login
                    tvfullname.text = it.name
                    follower.text = resources.getString(R.string.follower, it.followers)
                    following.text = resources.getString(R.string.follow, it.following)
                }
            }
        }

        detailViewModel.getDataByUsername(username).observe(this) {
            isFavorite = it.isNotEmpty()
            val favoriteUser = UserEntity(username, avatar)
            if (it.isEmpty()) {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fab.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.fab.contentDescription = getString(R.string.favorite_add)
            } else {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fab.context,
                        R.drawable.ic_favorite_yellow
                    )
                )
                binding.fab.contentDescription = getString(R.string.favorite_remove)
            }

            binding.fab.setOnClickListener {
                if (isFavorite) {
                    detailViewModel.deleteDataUser(favoriteUser)
                    Toast.makeText(this, R.string.favorite_remove, Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insertDataUser(favoriteUser)
                    Toast.makeText(this, R.string.favorite_add, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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
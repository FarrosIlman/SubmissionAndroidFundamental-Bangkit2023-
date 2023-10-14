package com.parrosz.submissiongithubuser.ui

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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.parrosz.submissiongithubuser.R
import com.parrosz.submissiongithubuser.adapter.PagerAdapter
import com.parrosz.submissiongithubuser.data.database.entity.UserEntity
import com.parrosz.submissiongithubuser.databinding.ActivityDetailUserBinding
import com.parrosz.submissiongithubuser.model.DetailUserModel

class detailUSer : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailUserModel> {
        DetailUserModel.ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER) ?: ""
        val avatar = intent.getStringExtra(EXTRA_AVATAR) ?: ""
        val urlHtml = intent.getStringExtra(EXTRA_URL) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        supportActionBar?.title = getString(R.string.user)

        detailViewModel.getDetailUsers(username)
        showLoading(true)

        detailViewModel.detailUser.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    Glide.with(this@detailUSer)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivPicture)
                    name.text = it.name
                    url1.text = it.htmlUrl
                    repository.text = resources.getString(R.string.repos, it.repo)
                    follower.text = resources.getString(R.string.follower, it.followers)
                    following.text = resources.getString(R.string.follow, it.following)
                }
            }
        }

        detailViewModel.getDataByUsername(username).observe(this) {
            isFavorite = it.isNotEmpty()
            val favoriteUser = UserEntity(username, avatar, urlHtml)
            if (it.isEmpty()) {
                binding.favoriteIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.favoriteIcon.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.favoriteIcon.contentDescription = getString(R.string.favorite_add)
            } else {
                binding.favoriteIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.favoriteIcon.context,
                        R.drawable.ic_favorite
                    )
                )
                binding.favoriteIcon.contentDescription = getString(R.string.favorite_remove)
            }

            binding.favoriteIcon.setOnClickListener {
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

        /*detailViewModel.errorMessage.observe(this) {
            binding.apply {
                tvErrorMessage.text = it
                tvErrorMessage.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }*/

        supportActionBar?.elevation = 0f

        val sectionsPagerAdapter = PagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {
                tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
package com.parrosz.submissiongithubuser.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parrosz.submissiongithubuser.R
import com.parrosz.submissiongithubuser.data.database.entity.UserEntity
import com.parrosz.submissiongithubuser.databinding.ActivityFavoriteBinding
import com.parrosz.submissiongithubuser.model.FavoriteViewModel
import com.parrosz.submissiongithubuser.ui.detailUSer

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = getString(R.string.favorite)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteData().observe(this) {
            setFavoriteData(it)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setFavoriteData(userEntities: List<UserEntity>) {
        val items = arrayListOf<UserEntity>()
        userEntities.map {
            val item = UserEntity(
                username = it.username,
                avatar = it.avatar,
                urlHtml = it.urlHtml
            )
            items.add(item)
        }
        val adapter = FavoriteAdapter(items)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                startActivity(
                    Intent(this@FavoriteActivity,detailUSer::class.java)
                        .putExtra(detailUSer.EXTRA_USER, data.username)
                        .putExtra(detailUSer.EXTRA_AVATAR, data.avatar)
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
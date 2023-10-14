package com.parrosz.submissiongithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.parrosz.submissiongithubuser.R
import com.parrosz.submissiongithubuser.adapter.UserAdapter
import com.parrosz.submissiongithubuser.data.response.ItemsItem
import com.parrosz.submissiongithubuser.databinding.ActivityMainBinding
import com.parrosz.submissiongithubuser.favorite.FavoriteActivity
import com.parrosz.submissiongithubuser.model.MainViewModel
import com.parrosz.submissiongithubuser.settings.DarkTheme
import com.parrosz.submissiongithubuser.settings.SettingsPreference
import com.parrosz.submissiongithubuser.settings.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            searchView.setupWithSearchBar(SearchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = binding.searchView.text.toString()
                    mainViewModel.searchUser(query)
                    SearchBar.text = searchView.text
                    binding.searchView.hide()
                    true
                }
            SearchBar.inflateMenu(R.menu.search)
            SearchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId){
                    R.id.favoritUser -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.theme_settings -> {
                        val intent = Intent(this@MainActivity, DarkTheme::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@MainActivity, detailUSer::class.java).also {
                    it.putExtra(detailUSer.EXTRA_USER, data.login)
                    it.putExtra(detailUSer.EXTRA_AVATAR, data.avatarUrl)
                    it.putExtra(detailUSer.EXTRA_URL, data.htmlUrl)
                    it.putExtra(Intent.EXTRA_TITLE, data.login)
                    startActivity(it)
                }
            }
        })

        val modePreferences = SettingsPreference.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(
            this,
            MainViewModel.ViewModelFactory(modePreferences)
        )[MainViewModel::class.java]

        modeViewModel.getTheme().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.listUser.observe(this) { list ->
            adapter.UserList(list)
        }

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.SearchBar.menu.findItem(R.id.favoritUser)?.setIcon(R.drawable.ic_favorite_white)
            binding.SearchBar.menu.findItem(R.id.theme_settings)?.setIcon(R.drawable.baseline_bedtime_24)
        } else {
            binding.SearchBar.menu.findItem(R.id.favoritUser)?.setIcon(R.drawable.ic_favorite)
            binding.SearchBar.menu.findItem(R.id.theme_settings)?.setIcon(R.drawable.baseline_wb_sunny_24)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
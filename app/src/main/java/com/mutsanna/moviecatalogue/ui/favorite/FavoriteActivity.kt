package com.mutsanna.moviecatalogue.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagerAdapter = FavoriteSectionPagerAdapter(this, supportFragmentManager)
        binding.favoriteViewPager.adapter = sectionPagerAdapter
        binding.favoriteTabs.setupWithViewPager(binding.favoriteViewPager)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = getString(R.string.favorite_section)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
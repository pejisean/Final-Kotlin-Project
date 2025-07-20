package com.example.kotlin_final_project

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem // Import MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_final_project.databinding.AboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: AboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        // This line shows the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    // You need to re-add this method to handle the back arrow click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // This will simulate a back press, navigating to the previous activity
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
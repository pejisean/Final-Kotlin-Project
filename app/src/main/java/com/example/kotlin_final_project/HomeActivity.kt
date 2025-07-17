package com.example.kotlin_final_project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import com.example.kotlin_final_project.databinding.HomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userMenuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_view_account -> {
                        Toast.makeText(this, "View Account clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_logout -> {
                        Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            val nightModeItem = popupMenu.menu.findItem(R.id.action_night_mode)
            val nightModeSwitch = nightModeItem.actionView?.findViewById<SwitchCompat>(R.id.night_mode_switch)

            nightModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
                val mode = if (isChecked) "Night Mode ON" else "Night Mode OFF"
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show()
                nightModeItem.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
            }

            // Show the popup menu
            popupMenu.show()
        }
    }
}
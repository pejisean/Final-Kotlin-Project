package com.example.kotlin_final_project

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Corrected this line to point to your home.xml file
        setContentView(R.layout.home)

        val userMenuIcon = findViewById<ImageButton>(R.id.user_menu_icon)

        userMenuIcon.setOnClickListener { view ->
            // Create a PopupMenu
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

            // Set a listener for menu item clicks
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

            // Handle the switch separately since it's in a custom layout
            val nightModeItem = popupMenu.menu.findItem(R.id.action_night_mode)
            val nightModeSwitch = nightModeItem.actionView?.findViewById<SwitchCompat>(R.id.night_mode_switch)

            nightModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
                val mode = if (isChecked) "Night Mode ON" else "Night Mode OFF"
                Toast.makeText(this, mode, Toast.LENGTH_SHORT).show()
                // Keep the menu open after a click on the switch
                nightModeItem.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
            }

            // Show the popup menu
            popupMenu.show()
        }
    }
}

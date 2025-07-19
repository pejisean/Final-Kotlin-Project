package com.example.kotlin_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_final_project.databinding.AccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: AccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = AccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userEmail = intent.getStringExtra("USER_EMAIL")

        if (userEmail != null) {
            val currentUser = UserDatabase.users.find { it.email.equals(userEmail, ignoreCase = true) }

            if (currentUser != null) {
                binding.tvUserName.text = currentUser.name
                binding.tvUserEmail.text = currentUser.email
                binding.tvDateCreated.text = currentUser.creationDate
                binding.tvNotesCreatedCount.text = currentUser.notes.size.toString()
                // Set the deleted notes count
                binding.tvNotesDeletedCount.text = currentUser.deletedNotesCount.toString()
            } else {
                Toast.makeText(this, "Error: User not found.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Error: No user specified.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.userMenuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.account_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        finish()
                        true
                    }
                    R.id.action_logout -> {
                        Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}

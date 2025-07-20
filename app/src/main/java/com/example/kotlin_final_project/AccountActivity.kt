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

    //Declarations for Database
    private var userId: Int = -1
    private lateinit var dbManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = AccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user ID from intent
        userId = intent.getIntExtra("USER_ID", -1)
        dbManager = DatabaseManager(this)
        dbManager.open()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Fetch user details from the database
        val cursor = dbManager.userFetch()
        var userName = ""
        var userEmail = ""
        var creationDate = ""
        var userFound = false

        // Check if the cursor is not null and has data
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_ID))
                if (id == userId) {
                    userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_NAME))
                    userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_EMAIL))
                    creationDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_DATE))
                    userFound = true
                    break
                }
            } while (cursor.moveToNext())
        }
        cursor.close()


        if (userFound) {
            // Fetch notes count for this user
            val notesCursor = dbManager.noteFetch(userId)
            val notesCount = notesCursor.count
            notesCursor.close()

            binding.tvUserName.text = userName
            binding.tvUserEmail.text = userEmail
            binding.tvDateCreated.text = creationDate
            binding.tvNotesCreatedCount.text = notesCount.toString()
            binding.tvNotesDeletedCount.text = "N/A" // Not tracked in DB
        } else {
            Toast.makeText(this, "Error: User not found.", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        dbManager.close() // Close the database connection when the activity is destroyed
    }
}

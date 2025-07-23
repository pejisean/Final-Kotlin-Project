package com.example.kotlin_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_final_project.databinding.LoginBinding
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGoToSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show()
            return
        }

        val dbManager = DatabaseManager(this)
        dbManager.open()
        val cursor = dbManager.userFetch()
        val passwordHash = hashString(password)
        var userFound = false
        var userName = ""
        var userId: Int? = null

        if (cursor.moveToFirst()) {
            do {
                val dbEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_EMAIL))
                val dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_PASSWORD))
                val dbName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_NAME))
                val dbUserId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_ID))
                if (dbEmail.equals(email, ignoreCase = true) && dbPassword == passwordHash) {
                    userFound = true
                    userName = dbName
                    userId = dbUserId
                    break
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        dbManager.close()

        if (!userFound) {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Welcome, $userName!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("USER_EMAIL", email)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun hashString(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}

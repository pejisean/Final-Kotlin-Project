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

        val user = UserDatabase.users.find { it.email.equals(email, ignoreCase = true) }

        if (user == null) {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        val passwordHash = hashString(password)
        if (user.passwordHash == passwordHash) {
            Toast.makeText(this, "Welcome, ${user.name}!", Toast.LENGTH_SHORT).show()

            // Set the current user and pass their email to the next screen
            UserDatabase.currentUserEmail = user.email
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("USER_EMAIL", user.email)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hashString(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}

package com.example.kotlin_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_final_project.databinding.SignupBinding
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: SignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGoToSignup.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btn2signup.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleSignUp() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        if (UserDatabase.users.any { it.email.equals(email, ignoreCase = true) }) {
            Toast.makeText(this, "A user with this email already exists.", Toast.LENGTH_SHORT).show()
            return
        }

        val passwordHash = hashString(password)
        val creationDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val newUser = User(name, email, passwordHash, creationDate)
        UserDatabase.users.add(newUser)

        Toast.makeText(this, "Sign-up successful! Please log in.", Toast.LENGTH_LONG).show()

        val intent = Intent(this, LoginActivity::class.java)
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

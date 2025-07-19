package com.example.kotlin_final_project

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_final_project.databinding.AddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: AddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSaveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.etNoteTitle.text.toString().trim()
        val content = binding.etNoteContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, getString(R.string.note_empty_error), Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val newNote = Note(title, content, currentDate)

        val currentUserEmail = UserDatabase.currentUserEmail
        val user = UserDatabase.users.find { it.email.equals(currentUserEmail, ignoreCase = true) }
        user?.notes?.add(newNote)

        Toast.makeText(this, getString(R.string.note_saved_success), Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK)
        finish()
    }
}

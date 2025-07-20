package com.example.kotlin_final_project

import android.app.Activity
import android.content.Intent
import android.os.Build
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
    private var isEditing: Boolean = false
    private var currentNoteId: String? = null // To store the ID of the note being edited

    companion object {
        const val EXTRA_NOTE = "extra_note" // Key to pass Note object when editing
        const val REQUEST_CODE_ADD_NOTE = 1
        const val REQUEST_CODE_EDIT_NOTE = 2
    }

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

        // Retrieve the Note object if in edit mode
        val noteToEdit: Note? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_NOTE, Note::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_NOTE) as? Note
        }

        noteToEdit?.let {
            isEditing = true
            currentNoteId = it.id // Store the ID of the note being edited

            // Pre-fill the EditTexts with existing note data
            binding.etNoteTitle.setText(it.title)
            binding.etNoteContent.setText(it.content)

            // Change button text for better UX
            binding.btnSaveNote.text = getString(R.string.update_note_button) // Make sure this string resource exists
        } ?: run {
            // No Note object passed, so we are in "add new note" mode
            binding.btnSaveNote.text = getString(R.string.save_moment_button)
        }

        binding.btnSaveNote.setOnClickListener {
            saveOrUpdateNote()
        }
    }

    private fun saveOrUpdateNote() {
        val title = binding.etNoteTitle.text.toString().trim()
        val content = binding.etNoteContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, getString(R.string.note_empty_error), Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val currentUserEmail = UserDatabase.currentUserEmail // Assuming UserDatabase is accessible
        val user = UserDatabase.users.find { it.email.equals(currentUserEmail, ignoreCase = true) }

        if (isEditing && currentNoteId != null) {
            // Find the note in the user's list by its ID and update its properties
            val noteToUpdate = user?.notes?.find { it.id == currentNoteId }
            if (noteToUpdate != null) {
                noteToUpdate.title = title
                noteToUpdate.content = content
                // noteToUpdate.date = currentDate // Uncomment if you want to update the date on edit
                Toast.makeText(this, getString(R.string.note_updated_success), Toast.LENGTH_SHORT).show() // Make sure this string resource exists
            } else {
                Toast.makeText(this, "Error: Note not found for update.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Create a new note
            val newNote = Note(title = title, content = content, date = currentDate)
            user?.notes?.add(newNote)
            Toast.makeText(this, getString(R.string.note_saved_success), Toast.LENGTH_SHORT).show()
        }

        setResult(Activity.RESULT_OK) // Indicate success to the calling activity
        finish() // Close the activity
    }
}
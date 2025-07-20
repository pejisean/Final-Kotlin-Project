package com.example.kotlin_final_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_final_project.databinding.HomeBinding
import com.example.kotlin_final_project.DatabaseManager

class HomeActivity : AppCompatActivity(), NoteAdapter.OnNoteActionsListener {

    private lateinit var binding: HomeBinding
    private var currentUser: User? = null
    private lateinit var noteAdapter: NoteAdapter // Declare NoteAdapter here

    private var userId: Int = -1
    private lateinit var dbManager: DatabaseManager

    // Launcher for Add/Edit Note Activity
    private val addEditNoteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Refresh notes if AddNoteActivity finished successfully (note added or edited)
            loadNotes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Opening the database
        userId = intent.getIntExtra("USER_ID", -1)
        dbManager = DatabaseManager(this)
        dbManager.open()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userEmail = intent.getStringExtra("USER_EMAIL")
        currentUser = UserDatabase.users.find { it.email.equals(userEmail, ignoreCase = true) }

        setupRecyclerView() // Setup RecyclerView and initialize adapter
        loadNotes() // Load initial notes into the adapter

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("USER_ID", userId) // Pass user ID to AddNoteActivity
            addEditNoteResultLauncher.launch(intent)
        }

        binding.userMenuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_view_account -> {
                        val intent = Intent(this, AccountActivity::class.java)
                        intent.putExtra("USER_EMAIL", currentUser?.email)
                        startActivity(intent)
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

    private fun setupRecyclerView() {
        binding.diaryRecyclerView.layoutManager = LinearLayoutManager(this)
        // Initialize the adapter once with an empty list.
        // Data will be populated via updateNotes() in loadNotes().
        noteAdapter = NoteAdapter(mutableListOf(), this) // Pass 'this' as OnNoteActionsListener
        binding.diaryRecyclerView.adapter = noteAdapter
    }

    private fun loadNotes() {
        // Fetch notes from the database for the current user
        val cursor = dbManager.noteFetch(userId)
        val notes = mutableListOf<Note>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTES_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTES_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTES_CONTENT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTES_DATE))
            notes.add(Note(id = id.toString(), title = title, content = content, date = date))
        }
        cursor.close()
        noteAdapter.updateNotes(notes.reversed())

        // Check if notes list is empty and update UI accordingly
        if (notes.isEmpty()) {
            binding.emptyDiaryText.visibility = View.VISIBLE
            binding.diaryRecyclerView.visibility = View.GONE
        } else {
            binding.emptyDiaryText.visibility = View.GONE
            binding.diaryRecyclerView.visibility = View.VISIBLE
        }

    }

    // --- OnNoteActionsListener Implementations ---

    override fun onEditNote(note: Note) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra(AddNoteActivity.EXTRA_NOTE, note)
        intent.putExtra("USER_ID", userId) // Pass user ID to AddNoteActivity
        addEditNoteResultLauncher.launch(intent) //Launch AddNoteActivity in edit mode
    }

    override fun onDeleteNote(note: Note) {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()

        // Set up click listeners for dialog buttons
        dialogView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            dbManager.noteDelete(note.id.toInt()) // Delete the note from the database
            loadNotes()
            Toast.makeText(this, getString(R.string.moment_deleted_toast), Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            alertDialog.dismiss() // Dismiss the dialog without deleting
        }
    }
}
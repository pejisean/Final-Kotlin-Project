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

class HomeActivity : AppCompatActivity(), NoteAdapter.OnNoteActionsListener {

    private lateinit var binding: HomeBinding
    private var currentUser: User? = null

    private val addNoteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadNotes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userEmail = intent.getStringExtra("USER_EMAIL")
        currentUser = UserDatabase.users.find { it.email.equals(userEmail, ignoreCase = true) }

        setupRecyclerView()
        loadNotes()

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            addNoteResultLauncher.launch(intent)
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
    }

    private fun loadNotes() {
        currentUser?.let { user ->
            if (user.notes.isEmpty()) {
                binding.emptyDiaryText.visibility = View.VISIBLE
                binding.diaryRecyclerView.visibility = View.GONE
            } else {
                binding.emptyDiaryText.visibility = View.GONE
                binding.diaryRecyclerView.visibility = View.VISIBLE
                binding.diaryRecyclerView.adapter = NoteAdapter(user.notes.reversed(), this)
            }
        }
    }

    override fun onEditNote(note: Note) {
        // TODO: Later, this will open an edit screen.
        Toast.makeText(this, "Edit: ${note.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteNote(note: Note) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()

        dialogView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            currentUser?.notes?.remove(note)
            currentUser?.let { user ->
                user.deletedNotesCount++
            }
            loadNotes()
            Toast.makeText(this, getString(R.string.moment_deleted_toast), Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            alertDialog.dismiss()
        }
    }
}

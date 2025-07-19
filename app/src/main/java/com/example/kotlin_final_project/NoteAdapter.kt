package com.example.kotlin_final_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_final_project.databinding.ItemNoteBinding

class NoteAdapter(
    private val notes: List<Note>,
    private val listener: OnNoteActionsListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var expandedPosition = -1

    interface OnNoteActionsListener {
        fun onEditNote(note: Note)
        fun onDeleteNote(note: Note)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, position: Int) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteDate.text = note.date
            binding.tvNoteContentSnippet.text = note.content

            val isExpanded = position == expandedPosition
            binding.tvNoteContentSnippet.maxLines = if (isExpanded) Integer.MAX_VALUE else 3

            itemView.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                notifyDataSetChanged()
            }

            binding.btnNoteMenu.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menuInflater.inflate(R.menu.note_item_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit_note -> {
                            listener.onEditNote(note)
                            true
                        }
                        R.id.action_delete_note -> {
                            listener.onDeleteNote(note)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position], position)
    }

    override fun getItemCount(): Int = notes.size
}

package com.internshala.notes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.internshala.notes.R
import com.internshala.notes.data.models.Note
import com.internshala.notes.databinding.ItemNoteBinding

class NotesAdapter(
    itemClickListener: ItemClickListener
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(DiffUtilCallback()) {

    private val _itemClickListener = itemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent, _itemClickListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        return holder.bind(item)
    }

    class NoteViewHolder(
        binding: ItemNoteBinding,
        itemClickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val _binding: ItemNoteBinding = binding

        private val _itemClickListener = itemClickListener

        init {
            // Click listener
            _binding.root.setOnClickListener(this)
            _binding.noteOptionsMenu.setOnClickListener(this)
            // Long-press
            _binding.root.setOnLongClickListener {
                _itemClickListener.onItemClick(_note, CLICK_ID_SELECT)
                true
            }
        }

        fun bind(note: Note) {
            _binding.note = note
        }

        private val _note get() = _binding.note!!

        override fun onClick(v: View?) {
            v?.let {
                if (v.id == R.id.note_options_menu) {
                    displayNotesOptionsPopup(v)
                } else {
                    _itemClickListener.onItemClick(_note, CLICK_ID_EDIT)
                }
            }
        }

        private fun displayNotesOptionsPopup(v: View) {
            val popup = PopupMenu(v.context, v)

            when {
                _note.isArchived() -> {
                    popup.inflate(R.menu.arhived_note_options)
                }
                _note.isBinned() -> {
                    popup.inflate(R.menu.binned_note_options)
                }
                else -> {
                    popup.inflate(R.menu.note_options)
                }
            }
            popup.setOnMenuItemClickListener { item ->
                _itemClickListener.onItemClick(
                    _note, when (item.itemId) {
                        R.id.archive -> {
                            CLICK_ID_ARCHIVE
                        }
                        R.id.bin -> {
                            CLICK_ID_MOVE_TO_BIN
                        }
                        R.id.restore -> {
                            CLICK_ID_RESTORE
                        }
                        R.id.unarchive -> {
                            CLICK_ID_UNARCHIVE
                        }
                        R.id.delete -> {
                            CLICK_ID_DELETE
                        }
                        else -> {
                            CLICK_ID_EDIT
                        }
                    }
                )
                popup.dismiss()
                true
            }
            popup.show()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: ItemClickListener,
            ): NoteViewHolder {
                val inflater = LayoutInflater.from(parent.context)

                val binding = DataBindingUtil.inflate<ItemNoteBinding>(
                    inflater,
                    R.layout.item_note,
                    parent,
                    false
                )

                return NoteViewHolder(binding, itemClickListener)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val CLICK_ID_EDIT = 0
        const val CLICK_ID_SELECT = 1
        const val CLICK_ID_DELETE = 2
        const val CLICK_ID_MOVE_TO_BIN = 3
        const val CLICK_ID_ARCHIVE = 4
        const val CLICK_ID_UNARCHIVE = 5
        const val CLICK_ID_RESTORE = 6
    }

    interface ItemClickListener {
        fun onItemClick(note: Note, clickId: Int)
    }
}
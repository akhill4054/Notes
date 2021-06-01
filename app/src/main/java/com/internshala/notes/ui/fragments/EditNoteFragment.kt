package com.internshala.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.internshala.notes.R
import com.internshala.notes.data.models.Note
import com.internshala.notes.databinding.FragmentEditNoteBinding
import com.internshala.notes.ui.interfaces.MainActivityListener
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.AppUiHelper
import com.internshala.notes.viewmodels.EditNoteViewModel


private const val ARG_NOTE = "note"

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null

    private val binding get() = _binding!!

    private val _viewModel: EditNoteViewModel by activityViewModels()

    private lateinit var _activityListener: MainActivityListener
    private lateinit var _snackBarListener: SnackBarListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _activityListener = context as MainActivityListener
        _snackBarListener = context as SnackBarListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val note: Note? = arguments?.getParcelable(ARG_NOTE)
        _viewModel.setCurrentEditableNote(note)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_note,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup views here
        binding.editTextTitle.setOnEditorActionListener { _, actionId, _ ->
            // Triggered when done editing (as clicked done on keyboard)
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editTextTitle.clearFocus()
            }
            false
        }

        // Click listeners
        binding.toolbar.setNavigationOnClickListener {
            _activityListener.navigateBack()
        }
        // Note options menu
        binding.toolbar.setOnMenuItemClickListener { item ->
            val note = binding.note ?: return@setOnMenuItemClickListener false

            when (item.itemId) {
                R.id.archive -> {
                    _viewModel.archive(note)
                    // Show message
                    _snackBarListener.onShowSnackBar(
                        getString(R.string.msg_note_archived)
                    )
                }
                R.id.bin -> {
                    _viewModel.bin(note)
                    // Show message
                    _snackBarListener.onShowSnackBar(
                        getString(R.string.msg_note_binned)
                    )
                }
                R.id.unarchive -> {
                    _viewModel.unarchive(note)
                    // Show message
                    _snackBarListener.onShowSnackBar(
                        getString(R.string.msg_note_unarchived)
                    )
                }
                R.id.delete -> {
                    AppUiHelper.showDeleteConfirmation(requireContext()) {
                        _viewModel.delete(note)
                        // Show message
                        _snackBarListener.onShowSnackBar(
                            getString(R.string.msg_note_deleted)
                        )
                        _activityListener.navigateBack()
                    }
                    return@setOnMenuItemClickListener true
                }
            }
            _activityListener.navigateBack()
            true
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        _viewModel.note.observe(viewLifecycleOwner, { note ->
            if (note.createdOn == null) {
                // Fresh note, show keyboard
                binding.editTextNote.requestFocus()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
            // Setup toolbar menu
            val menu = binding.toolbar.menu

            // As archived notes are allowed to be edited
            if (note.isArchived()) {
                menu.removeItem(R.id.archive)
            } else {
                menu.removeItem(R.id.unarchive)
            }

            binding.note = note
        })
    }

    override fun onDestroyView() {
        // Saving note
        val title = binding.editTextTitle.text.toString()
        val noteText = binding.editTextNote.text.toString()

        _viewModel.saveNote(title, noteText)

        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(note: Note) =
            EditNoteFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                }
            }
    }
}
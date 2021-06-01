package com.internshala.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.internshala.notes.R
import com.internshala.notes.data.models.Note
import com.internshala.notes.databinding.FragmentBinBinding
import com.internshala.notes.ui.adapters.NotesAdapter
import com.internshala.notes.ui.interfaces.MainActivityListener
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.AppUiHelper
import com.internshala.notes.viewmodels.BinnedNotesViewModel
import com.internshala.notes.viewmodels.EditNoteViewModel

class BinFragment : Fragment(), NotesAdapter.ItemClickListener {

    private var _binding: FragmentBinBinding? = null

    private val binding get() = _binding!!

    private val _editNoteViewModel: EditNoteViewModel by activityViewModels()
    private val _binViewModel: BinnedNotesViewModel by viewModels()

    private lateinit var _notesAdapter: NotesAdapter

    private lateinit var _activityListener: MainActivityListener
    private lateinit var _snackBarListener: SnackBarListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _activityListener = context as MainActivityListener
        _snackBarListener = context as SnackBarListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bin,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup views here
        // Click listeners
        binding.toolbar.setNavigationOnClickListener {
            _activityListener.navigateBack()
        }

        // Notes recycler
        _notesAdapter = NotesAdapter(this)
        binding.notes.adapter = _notesAdapter
        binding.notes.layoutManager = StaggeredGridLayoutManager(
            2, LinearLayoutManager.VERTICAL
        )

        subscribeObservers()
    }

    private fun subscribeObservers() {
        _binViewModel.archivedNotes.observe(viewLifecycleOwner, { notes ->
            binding.noBinnedNotes = notes.isEmpty()
            _notesAdapter.submitList(notes)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(note: Note, clickId: Int) {
        when (clickId) {
            NotesAdapter.CLICK_ID_EDIT -> {
                // Open in edit mode
                _snackBarListener.onShowSnackBar(
                    "Edit is not allowed on deleted notes!"
                )
            }
            NotesAdapter.CLICK_ID_RESTORE -> {
                _editNoteViewModel.restore(note)
                // Show message
                _snackBarListener.onShowSnackBar(
                    getString(R.string.msg_note_restored)
                )
            }
            NotesAdapter.CLICK_ID_DELETE -> {
                AppUiHelper.showDeleteConfirmation(requireContext()) {
                    _editNoteViewModel.delete(note)
                    // Show message
                    _snackBarListener.onShowSnackBar(
                        getString(R.string.msg_note_deleted)
                    )
                }
            }
        }
    }
}
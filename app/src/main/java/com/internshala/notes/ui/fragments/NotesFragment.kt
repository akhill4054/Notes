package com.internshala.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.internshala.notes.R
import com.internshala.notes.data.models.Note
import com.internshala.notes.data.models.User
import com.internshala.notes.databinding.FragmentNotesBinding
import com.internshala.notes.ui.adapters.NotesAdapter
import com.internshala.notes.ui.adapters.UserListAdapter
import com.internshala.notes.ui.dialogs.ConfirmLogoutBottomSheet
import com.internshala.notes.ui.dialogs.LoginBottomSheet
import com.internshala.notes.ui.dialogs.NotesExtraOptionsPopup
import com.internshala.notes.ui.dialogs.UserAccountDialog
import com.internshala.notes.ui.interfaces.MainActivityListener
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.AppUiHelper
import com.internshala.notes.viewmodels.*

class NotesFragment : Fragment(), View.OnClickListener,
    UserListAdapter.ItemClickListener, UserAccountDialog.AccountDialogListener,
    NotesAdapter.ItemClickListener {

    private var _binding: FragmentNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var _context: Context

    // ViewModels
    private val _userViewModel: UserViewModel by activityViewModels()
    private val _appViewModel: AppViewModel by activityViewModels()
    private val _editNoteViewModel: EditNoteViewModel by activityViewModels()
    private val _notesViewModel: NotesViewModel by viewModels()

    // Adapters
    private val _userAdapter by lazy { UserListAdapter(this) }
    private val _notesAdapter by lazy { NotesAdapter(this) }

    // Dialogue/popups
    private val _userAccountDialog by lazy {
        UserAccountDialog(
            _context,
            binding.user!!,
            _userAdapter,
            this
        )
    }

    // Listeners
    private lateinit var _snackBarListener: SnackBarListener
    private lateinit var _activityListener: MainActivityListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = context

        _snackBarListener = context as SnackBarListener
        _activityListener = context as MainActivityListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notes,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        subscribeObservers()
    }

    private fun setupViews() {
        // Note search
        binding.searchNotes.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                _notesViewModel.searchNotes(query)
                return true
            }
        })

        // Notes recycler
        binding.notes.adapter = _notesAdapter
        binding.notes.layoutManager = StaggeredGridLayoutManager(
            2, LinearLayoutManager.VERTICAL
        )

        // Click listeners
        binding.themeButton.setOnClickListener(this)
        binding.accountOptions.setOnClickListener(this)
        binding.notesExtraOptions.setOnClickListener(this)
        binding.addNote.setOnClickListener(this)
    }

    private fun subscribeObservers() {
        // App configurations
        _appViewModel.isDarkMode.observe(viewLifecycleOwner, { isDarkMode ->
            binding.isDarkMode = isDarkMode
        })

        // User
        _userViewModel.user.observe(viewLifecycleOwner, { currentUser ->
            binding.user = currentUser
        })

        _userViewModel.otherUsers.observe(viewLifecycleOwner, { otherUsers ->
            _userAdapter.submitList(otherUsers)
        })

        // User notes
        _notesViewModel.userNotes.observe(viewLifecycleOwner, { notes ->
            notes?.let {
                binding.isNotesEmpty = notes.isEmpty()
                _notesAdapter.submitList(notes)
            }
        })
        // Searched notes
        _notesViewModel.searchResult.observe(viewLifecycleOwner, { notes ->
            notes?.let {
                binding.isNotesEmpty = notes.isEmpty()
                _notesAdapter.submitList(notes)
            }
        })

        // Save/edit status
        _editNoteViewModel.resetStatus()
        _editNoteViewModel.noteEditStatus.observe(viewLifecycleOwner, { status ->
            when (status) {
                is NoteSaved -> {
                    showSnackBar(getString(R.string.msg_note_saved))
                }
                is NoteModified -> {
                    showSnackBar(getString(R.string.msg_note_modified))
                }
                is EmptyNoteDiscarded -> {
                    showSnackBar(getString(R.string.msg_note_discarded))
                }
                else -> {
                }
            }
        })
    }

    private fun showSnackBar(message: String) {
        _snackBarListener.onShowSnackBar(
            message,
            anchorView = binding.addNote
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Interface listeners
    override fun onUserSelected(user: User) {
        _userAccountDialog.dismiss()
        // Request to switch user
        _userViewModel.switchUser(user)
    }

    override fun onLogOutRequested() {
        _userAccountDialog.dismiss()

        ConfirmLogoutBottomSheet().show(
            childFragmentManager,
            ConfirmLogoutBottomSheet::class.java.simpleName
        )
    }

    override fun onNewLoginRequested() {
        _userAccountDialog.dismiss()

        LoginBottomSheet().show(
            childFragmentManager,
            LoginBottomSheet::class.java.simpleName
        )
    }

    override fun onItemClick(note: Note, clickId: Int) {
        when (clickId) {
            NotesAdapter.CLICK_ID_EDIT -> {
                // Open in edit mode
                _activityListener.navigateTo(
                    EditNoteFragment.newInstance(note)
                )
            }
            NotesAdapter.CLICK_ID_MOVE_TO_BIN -> {
                _editNoteViewModel.bin(note)
                // Show message
                _snackBarListener.onShowSnackBar(
                    getString(R.string.msg_note_binned)
                )
            }
            NotesAdapter.CLICK_ID_ARCHIVE -> {
                _editNoteViewModel.archive(note)
                // Show message
                _snackBarListener.onShowSnackBar(
                    getString(R.string.msg_note_archived)
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
            NotesAdapter.CLICK_ID_SELECT -> {
                // Here comes the selection tracker!
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.add_note -> {
                _activityListener.navigateTo(EditNoteFragment())
            }
            R.id.theme_button -> {
                _appViewModel.toggleDarkMode()
            }
            R.id.account_options -> {
                _userAccountDialog.show()
            }
            R.id.notes_extra_options -> {
                displayNotesExtraOptionsPopup()
            }
        }
    }

    private fun displayNotesExtraOptionsPopup() {
        NotesExtraOptionsPopup(
            binding.notesExtraOptions,
            object : NotesExtraOptionsPopup.ItemClickListener {
                override fun onOpenArchive() {
                    _activityListener.navigateTo(
                        ArchiveFragment()
                    )
                }

                override fun onOpenBin() {
                    _activityListener.navigateTo(
                        BinFragment()
                    )
                }
            }
        ).show()
    }
}
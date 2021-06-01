package com.internshala.notes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.internshala.notes.R
import com.internshala.notes.data.models.User
import com.internshala.notes.databinding.DialogUserAccountsBinding
import com.internshala.notes.ui.adapters.UserListAdapter

class UserAccountDialog(
    context: Context,
    currentUser: User,
    userListAdapter: UserListAdapter,
    dialogListener: AccountDialogListener
) {

    private val _dialog: AlertDialog

    init {
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<DialogUserAccountsBinding>(
            inflater,
            R.layout.dialog_user_accounts,
            null,
            false
        )

        _dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .create()

        // Setup data-binding object here.
        binding.user = currentUser

        // User list adapter
        binding.users.adapter = userListAdapter

        // Click listeners
        binding.currentUser.removeAccount.setOnClickListener {
            dialogListener.onLogOutRequested()
        }
        binding.addAnotherAccount.root.setOnClickListener {
            dialogListener.onNewLoginRequested()
        }
        binding.close.setOnClickListener {
            _dialog.dismiss()
        }
    }

    fun show() {
        _dialog.show()
    }

    fun dismiss() {
        _dialog.dismiss()
    }

    interface AccountDialogListener {
        fun onLogOutRequested()
        fun onNewLoginRequested()
    }
}
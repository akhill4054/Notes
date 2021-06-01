package com.internshala.notes.utils

import android.content.Context
import com.internshala.notes.R
import com.internshala.notes.ui.dialogs.ConfirmationDialog

class AppUiHelper {
    companion object {
        fun showDeleteConfirmation(context: Context, onDelete: () -> Unit) {
            ConfirmationDialog(context)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.msg_permanent_delete)
                .setActionIcon(R.drawable.ic_delete_forever_24)
                .setActionButton(R.string.delete, onDelete)
                .show()
        }
    }
}
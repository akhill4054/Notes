package com.internshala.notes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.internshala.notes.databinding.DialogConfirmActionBinding

class ConfirmationDialog(context: Context) {

    private val _context = context

    private val _binding = DialogConfirmActionBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )
    private val _dialog = MaterialAlertDialogBuilder(context)
        .setView(_binding.root)
        .create()

    init {
        // Default click listeners
        _binding.close.setOnClickListener {
            _dialog.dismiss()
        }
        _binding.secondaryButton.setOnClickListener {
            _dialog.dismiss()
        }
    }

    fun setTitle(titleId: Int): ConfirmationDialog {
        _binding.textTitle.text = _context.getString(titleId)
        return this
    }

    fun setMessage(messageId: Int): ConfirmationDialog {
        _binding.textTitle.text = _context.getString(messageId)
        return this
    }

    fun setActionIcon(resId: Int): ConfirmationDialog {
        _binding.icon.setImageResource(resId)
        return this
    }

    fun setActionButton(textResId: Int, onAction: () -> Unit): ConfirmationDialog {
        _binding.actionButton.text = _context.getString(textResId)
        _binding.actionButton.setOnClickListener {
            onAction()
            dismiss()
        }
        return this
    }

    fun setSecondaryButton(textResId: Int, onAction: () -> Unit): ConfirmationDialog {
        _binding.secondaryButton.text = _context.getString(textResId)
        _binding.secondaryButton.setOnClickListener {
            onAction()
            dismiss()
        }
        return this
    }

    fun show() {
        _dialog.show()
    }

    fun dismiss() {
        _dialog.dismiss()
    }
}
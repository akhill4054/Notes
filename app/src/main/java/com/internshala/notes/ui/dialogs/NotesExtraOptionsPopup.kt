package com.internshala.notes.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.internshala.notes.R
import com.internshala.notes.databinding.PopupNotesExtraOptionsBinding

class NotesExtraOptionsPopup(
    popupView: View,
    itemClickListener: ItemClickListener
) : View.OnClickListener {
    private val _popupView = popupView

    private val _context = popupView.context
    private val _popupWindow = PopupWindow(_context)
    private val _itemClickListener = itemClickListener

    private val _binding = PopupNotesExtraOptionsBinding.inflate(
        LayoutInflater.from(_context),
        null,
        false
    )

    init {
        _popupWindow.run {
            isFocusable = true
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            contentView = _binding.root
            elevation = 3F
            setBackgroundDrawable(null)
        }

        // Click listeners
        _binding.archive.setOnClickListener(this)
        _binding.bin.setOnClickListener(this)
    }

    fun show() {
        _popupWindow.showAsDropDown(_popupView)
    }

    interface ItemClickListener {
        fun onOpenArchive()
        fun onOpenBin()
    }

    override fun onClick(v: View?) {
        v!!.let {
            if (v.id == R.id.archive) {
                _itemClickListener.onOpenArchive()
            } else if (v.id == R.id.bin) {
                _itemClickListener.onOpenBin()
            }
        }
        // Hide popup
        _popupWindow.dismiss()
    }
}
package com.internshala.notes.ui.interfaces

import android.view.View

interface SnackBarListener {
    fun onShowSnackBar(
        message: String,
        isError: Boolean = false,
        anchorView: View? = null
    )
}
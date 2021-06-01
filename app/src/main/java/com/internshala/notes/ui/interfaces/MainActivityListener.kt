package com.internshala.notes.ui.interfaces

import androidx.fragment.app.Fragment

interface MainActivityListener {
    fun navigateBack()
    fun navigateTo(fragment: Fragment)
}
package com.internshala.notes.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.internshala.notes.R
import com.internshala.notes.databinding.ActivityMainBinding
import com.internshala.notes.ui.fragments.LoginFragment
import com.internshala.notes.ui.fragments.NotesFragment
import com.internshala.notes.ui.interfaces.MainActivityListener
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.FragmentTransitionHelper
import com.internshala.notes.viewmodels.UserViewModel

class MainActivity : AppCompatActivity(), SnackBarListener,
    MainActivityListener {

    private lateinit var _binding: ActivityMainBinding

    // ViewModels
    private val _userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        // Remove splash theme
        setTheme(R.style.AppTheme)
        setContentView(_binding.root)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        _userViewModel.user.observe(this, { user ->
            if (user != null) {
                // Navigate to notes screen
                displayNotesScreen()
            } else {
                // Navigate to login screen
                displayLoginScreen()
            }
        })
    }

    private fun displayLoginScreen() {
        val loginFragment = LoginFragment()

        FragmentTransitionHelper.replaceFragment(
            this,
            _binding.mainContainer,
            loginFragment
        )
    }

    private fun displayNotesScreen() {
        val notesFragment = NotesFragment()

        FragmentTransitionHelper.replaceFragment(
            this,
            _binding.mainContainer,
            notesFragment
        )
    }

    override fun onShowSnackBar(message: String, isError: Boolean, anchorView: View?) {
        val sb = Snackbar.make(
            _binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
        if (isError) {
            sb.setTextColor(ContextCompat.getColor(this, R.color.white))
            sb.setBackgroundTint(ContextCompat.getColor(this, R.color.error))
        }
        sb.anchorView = anchorView
        sb.show()
    }

    override fun navigateBack() {
        onBackPressed()
    }

    override fun navigateTo(fragment: Fragment) {
        FragmentTransitionHelper.addToBackStack(
            this,
            _binding.mainContainer,
            fragment
        )
    }
}
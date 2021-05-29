package com.internshala.notes.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.internshala.notes.R
import com.internshala.notes.databinding.ActivityMainBinding
import com.internshala.notes.ui.fragments.LoginFragment
import com.internshala.notes.ui.fragments.NotesFragment
import com.internshala.notes.utils.FragmentTransitionHelper
import com.internshala.notes.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    private val _userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
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
}
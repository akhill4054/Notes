package com.internshala.notes.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.internshala.notes.databinding.FragmentLoginBinding
import com.internshala.notes.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mContext: Context

    private val _viewModel: LoginViewModel by viewModels()

    // Google sign-in
    private val _gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private lateinit var _googleSignInClient: GoogleSignInClient

    private lateinit var _googleSignInIntentLauncher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context

        _googleSignInClient = GoogleSignIn.getClient(mContext, _gso)

        val googleSignInIntentContract = object : ActivityResultContract<Intent, Intent?>() {
            override fun createIntent(context: Context, input: Intent): Intent {
                return input
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                return intent
            }
        }

        val googleSignInResultCallback = ActivityResultCallback<Intent?> {
            it?.let { data ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Request viewModel to handle the result
                _viewModel.handleGoogleSignInResult(task)
            }
        }

        _googleSignInIntentLauncher = registerForActivityResult(
            googleSignInIntentContract,
            googleSignInResultCallback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup views
        // Google sign-in button
        val googleSignInButton = binding.googleSingInButton
        googleSignInButton.setSize(SignInButton.SIZE_WIDE)

        // Click listeners
        binding.googleSingInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        _googleSignInIntentLauncher
            .launch(_googleSignInClient.signInIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
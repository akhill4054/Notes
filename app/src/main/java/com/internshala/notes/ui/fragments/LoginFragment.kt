package com.internshala.notes.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.SignInButton
import com.internshala.notes.R
import com.internshala.notes.databinding.FragmentLoginBinding
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.GoogleSignInHelper
import com.internshala.notes.viewmodels.LoginStatus
import com.internshala.notes.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mContext: Context

    private val _viewModel: LoginViewModel by viewModels()

    // Google sign-in
    private val _googleSignInHelper by lazy {
        GoogleSignInHelper(mContext)
    }
    private lateinit var _googleSignInRequestLauncher: ActivityResultLauncher<Intent>

    private lateinit var _snackBarListener: SnackBarListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        try {
            _snackBarListener = context as SnackBarListener
        } catch (e: Exception) {
            throw RuntimeException("${context::class.simpleName} must implement ${SnackBarListener::class.simpleName}!")
        }
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

        _googleSignInRequestLauncher =
            _googleSignInHelper.getSignInIntentLauncher(this) { task ->
                // Handle sign-in task result
                _viewModel.handleGoogleSignInResult(task)
            }

        // Click listeners
        binding.googleSingInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Subscribe observers here
        _viewModel.loginStatus.observe(viewLifecycleOwner, { status ->
            if (status is LoginStatus.Error) {
                _snackBarListener.onShowSnackBar(getString(R.string.error_typical))
            }
        })
    }

    private fun signInWithGoogle() {
        _googleSignInHelper.signIn(_googleSignInRequestLauncher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
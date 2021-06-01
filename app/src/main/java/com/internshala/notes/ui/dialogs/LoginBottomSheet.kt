package com.internshala.notes.ui.dialogs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.viewModels
import com.google.android.gms.common.SignInButton
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.internshala.notes.R
import com.internshala.notes.databinding.DialogLoginBottomSheetBinding
import com.internshala.notes.ui.interfaces.SnackBarListener
import com.internshala.notes.utils.GoogleSignInHelper
import com.internshala.notes.viewmodels.LoginStatus
import com.internshala.notes.viewmodels.LoginViewModel

class LoginBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogLoginBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mContext: Context

    private val _viewModel: LoginViewModel by viewModels()

    private lateinit var _snackBarListener: SnackBarListener

    // Google sign-in
    private val _googleSignInHelper by lazy {
        GoogleSignInHelper(mContext)
    }
    private lateinit var _googleSignInRequestLauncher: ActivityResultLauncher<Intent>

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
        _binding = DialogLoginBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For rounded corners on top
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet =
                    dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?

                bottomSheet!!.setBackgroundResource(R.drawable.bg_bottom_sheet)
            }
        })

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
        binding.close.setOnClickListener {
            dismissAllowingStateLoss()
        }

        // Subscribe observers here
        _viewModel.loginStatus.observe(viewLifecycleOwner, { status ->
            if (status is LoginStatus.Error) {
                if (status.message != null) {
                    dismissAllowingStateLoss()
                    _snackBarListener.onShowSnackBar(status.message)
                } else {
                    _snackBarListener.onShowSnackBar(getString(R.string.error_typical))
                }
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
package com.internshala.notes.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.internshala.notes.R
import com.internshala.notes.databinding.DialogConfirmLogoutBottomSheetBinding
import com.internshala.notes.viewmodels.UserViewModel

class ConfirmLogoutBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogConfirmLogoutBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val _viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DialogConfirmLogoutBottomSheetBinding
                .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For rounded corners
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

        // For rounded corners on top
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Click listeners
        binding.cancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.logout.setOnClickListener {
            _viewModel.removeCurrentUser()
        }
        binding.close.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
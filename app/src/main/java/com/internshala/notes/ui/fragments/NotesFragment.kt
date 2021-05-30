package com.internshala.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.internshala.notes.databinding.FragmentNotesBinding
import com.internshala.notes.repositories.AuthRepository
import com.internshala.notes.repositories.Authenticated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var _context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOut.setOnClickListener {
            val authRepository = AuthRepository.getInstance(requireActivity().application)
            lifecycleScope.launch(Dispatchers.IO) {
                authRepository.logout((authRepository.authStatus.value!! as Authenticated).user)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
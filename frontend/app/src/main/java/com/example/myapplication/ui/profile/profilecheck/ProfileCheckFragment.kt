package com.example.myapplication.ui.profile.profilecheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentProfileCheckBinding

class ProfileCheckFragment : Fragment() {
    private var _binding: FragmentProfileCheckBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //itt le kell kérdezni a user adatait
        val displayName = "displayName"
        val email = "email"
        binding.textViewUsername.text = displayName
        binding.textViewEmailAddress.text = email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
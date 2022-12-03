package com.example.myapplication.ui.profile.profilecheck

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentProfileCheckBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileCheckFragment : Fragment() {

    private var _binding: FragmentProfileCheckBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCheckBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        user?.let {
            binding.textViewUsername.text = user.displayName
            binding.textViewEmailAddress.text = user.email
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
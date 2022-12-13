package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.auth.SignInActivity.Companion.token
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewHello.text = "Hello!"

        binding.constraintUpload.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_upload)
        }
        binding.constraintWebshop.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_webshop)
        }
        binding.constraintCart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_cart)
        }
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
        }
        binding.constraintLogout.setOnClickListener {
            token = ""
            val intent = Intent(view.context, SignInActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.myapplication.ui.profile.profilemenu

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
import com.example.myapplication.databinding.FragmentProfileMenuBinding

class ProfileMenuFragment : Fragment() {
    private var _binding: FragmentProfileMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdminPage.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_profile_menu_to_navigation_profile_admin_page)
        }

        binding.buttonCheckProfile.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_profile_menu_to_navigation_profile_check)
        }

        binding.buttonEditProfile.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_profile_menu_to_navigation_profile)
        }

        binding.buttonSignOut.setOnClickListener {

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
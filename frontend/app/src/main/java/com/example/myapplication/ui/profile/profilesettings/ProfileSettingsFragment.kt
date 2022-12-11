package com.example.myapplication.ui.profile.profilesettings

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class ProfileSettingsFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        root = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var success: Boolean = true
        var delete: Boolean = false

        //itt anno firebase-szel hint-nek beállítottam a jelenlegi username és email párost
        //ehhez le kell kérni ezeket itt
        val displayName = "displayName"
        val email = "email"
        binding.editTextMyProfileUsername.hint = displayName
        binding.editTextMyProfileEmailAddress.hint = email

        binding.buttonDeleteUser.setOnClickListener {
            delete = true

            //ki kell jelentkeztetni a felhasznalot

            //ha sikeres
            Toast.makeText(view.context, "User account deleted.", Toast.LENGTH_LONG).show()
            val intent = Intent(view.context, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSaveChanges.setOnClickListener{
            if(binding.editTextMyProfileUsername.text.isNotEmpty()){
                //itt majd el kell küldeni az új displayName-et a DB-nek
                //ezt az alábbi módon lehet kiszedni az edittext-ből
                //displayName = binding.editTextMyProfileUsername.text.toString()

            }
            if(binding.editTextMyProfileEmailAddress.text.isNotEmpty()){
                //itt majd el kell küldeni az új emailt-t a DB-nek
                //ezt az alábbi módon lehet kiszedni az edittext-ből
                //displayName = binding.editTextMyProfileEmailAddress.text.toString()
            }
            if(binding.editTextMyProfilePassword1.text.isNotEmpty()){
                if(binding.editTextMyProfilePassword2.text.isNotEmpty()){
                    if(binding.editTextMyProfilePassword1.text.toString() == binding.editTextMyProfilePassword2.text.toString()){
                        val newPassword = binding.editTextMyProfilePassword1.text.toString()
                        //itt majd hash-elni kell az új pw-t és akkor post-olni a db-nek

                    }
                    else{
                        success = false
                        Toast.makeText(view.context, "The passwords entered do not match", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    success = false
                    Toast.makeText(view.context, "Confirm your new password in the second field", Toast.LENGTH_SHORT).show()
                }
            }
            if(success && !delete)
                findNavController().navigate(R.id.action_navigation_profile_to_navigation_profile_menu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        root = binding.root
        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var success: Boolean = true
        var delete: Boolean = false

        val user = firebaseAuth.currentUser
        user?.let {
            binding.editTextMyProfileUsername.hint = user.displayName
            binding.editTextMyProfileEmailAddress.hint = user.email
        }

        binding.buttonDeleteUser.setOnClickListener {
            delete = true
            val user = Firebase.auth.currentUser!!

            firebaseAuth.signOut()

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                        Toast.makeText(view.context, "User account deleted.", Toast.LENGTH_LONG).show()
                    }
                }

            val intent = Intent(view.context, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSaveChanges.setOnClickListener{
            if(binding.editTextMyProfileUsername.text.isNotEmpty()){
                val profileUpdates = userProfileChangeRequest {
                    displayName = binding.editTextMyProfileUsername.text.toString()
                }

                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile updated.")
                        }
                        else
                        {
                            success = false
                            Toast.makeText(view.context, task.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
            if(binding.editTextMyProfileEmailAddress.text.isNotEmpty()){
                user!!.updateEmail(binding.editTextMyProfileEmailAddress.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User email address updated.")
                        }
                        else
                        {
                            success = false
                            Toast.makeText(view.context, task.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
            if(binding.editTextMyProfilePassword1.text.isNotEmpty()){
                if(binding.editTextMyProfilePassword2.text.isNotEmpty()){
                    if(binding.editTextMyProfilePassword1.text.toString() == binding.editTextMyProfilePassword2.text.toString()){
                        val newPassword = binding.editTextMyProfilePassword1.text.toString()
                        user!!.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User password updated.")
                                }
                                else
                                {
                                    success = false
                                    Toast.makeText(view.context, task.exception.toString(), Toast.LENGTH_LONG).show()
                                }
                            }
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
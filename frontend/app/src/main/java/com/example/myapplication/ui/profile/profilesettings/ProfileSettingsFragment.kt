package com.example.myapplication.ui.profile.profilesettings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.ui.profile.profilecheck.UserProfile
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ProfileSettingsFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var root: View
    companion object {
        var userId = "";
    }

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

        getData().start()

        binding.buttonDeleteUser.setOnClickListener {
            delete = true

            //ki kell jelentkeztetni a felhasznalot
            //ha sikeres

            deleteUser().start()

            Toast.makeText(view.context, "User account deleted.", Toast.LENGTH_LONG).show()
            val intent = Intent(view.context, SignInActivity::class.java)
            startActivity(intent)

        }

        binding.buttonDeleteUser.setOnClickListener {
            delete = true

            val intent = Intent(view.context, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSaveChanges.setOnClickListener{
            if(binding.editTextMyProfileUsername.text.isNotEmpty() &&
                binding.editTextMyProfileEmailAddress.text.isNotEmpty() &&
                binding.editTextMyProfilePassword1.text.isNotEmpty()){

                sendData(binding.editTextMyProfileEmailAddress.text.toString(),
                    binding.editTextMyProfilePassword1.text.toString(),
                    binding.editTextMyProfileUsername.text.toString()).start()

                if(binding.editTextMyProfilePassword1.text.isNotEmpty()){
                    if(binding.editTextMyProfilePassword2.text.isNotEmpty()){
                        if(binding.editTextMyProfilePassword1.text.toString() == binding.editTextMyProfilePassword2.text.toString()){
                            val newPassword = binding.editTextMyProfilePassword1.text.toString()

                            sendData(binding.editTextMyProfileEmailAddress.text.toString(),
                                binding.editTextMyProfileUsername.text.toString(),
                                binding.editTextMyProfilePassword1.text.toString())
                        }
                        else{
                            success = false
                            Toast.makeText(view.context, "The passwords entered do not match", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else {
                success = false
                Toast.makeText(view.context, "Fill the username, email and password fields", Toast.LENGTH_SHORT).show()
            }

            if(success && !delete)
                findNavController().navigate(R.id.action_navigation_profile_to_navigation_profile_menu)
        }
    }

    private fun getData(): Thread {
        return Thread {

            val url = URL("http://192.168.1.93/users")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            connection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            connection.setRequestProperty("x-access-tokens", SignInActivity.token)

            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = Gson().fromJson(inputStreamReader, UserProfile::class.java)

                binding.editTextMyProfileUsername.hint = response.name
                binding.editTextMyProfileEmailAddress.hint = response.email
                userId = response.public_id
                inputStreamReader.close()
                inputSystem.close()
            } else {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun deleteUser(): Thread {
        return Thread {
            val url = URL("http://192.168.1.93/users/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            connection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            connection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            connection.setRequestProperty("x-access-tokens", SignInActivity.token)

            if (connection.responseCode == 200) {
                Log.d("Connection", "200")
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = Gson().fromJson(inputStreamReader, UserCRUDEAnsw::class.java)

                inputStreamReader.close()
                inputSystem.close()
            } else {
                Log.d("ERROR", "Network error")
            }
        }
    }

    private fun sendData(email: String, pass: String, user: String): Thread {
        return Thread {
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", pass)
            jsonObject.put("name", user)

            val jsonObjectString = jsonObject.toString()

            val url = URL("http://192.168.1.93/users/modify")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "PUT"
            connection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            connection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            connection.doInput = true
            connection.doOutput = true
            connection.setRequestProperty("x-access-tokens", SignInActivity.token)

            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()


            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = Gson().fromJson(inputStreamReader, UserCRUDEAnsw::class.java)
                updateUI(connection.responseCode, response.message)
                inputStreamReader.close()
                inputSystem.close()
            }
        }
    }
    private fun updateUI(responseCode: Int, responseMsg: String) {
        activity?.runOnUiThread {
            kotlin.run {
                if (responseCode == 200) {
                    Toast.makeText(activity, responseMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, responseMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.myapplication.ui.profile.profilecheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentProfileCheckBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

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
        getData().start()

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
                Log.d("Connection", "200")
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = Gson().fromJson(inputStreamReader, UserProfile::class.java)

                binding.textViewUsername.text = response.name
                binding.textViewEmailAddress.text = response.email
                inputStreamReader.close()
                inputSystem.close()
            } else {
                Log.d("ERROR", "Network error")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
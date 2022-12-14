package com.example.myapplication.auth


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignUp.setOnClickListener {
            val username = binding.editTextTextPersonName.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()
            val confirmPass = binding.editTextTextConfirmPassword.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    sendData(email, pass,username).start()
                } else {
                    Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Make sure to fill every field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendData(email: String, pass: String, username: String): Thread {
        return Thread {

            val jsonObject = JSONObject()
            jsonObject.put("password", pass)
            jsonObject.put("name", username)
            jsonObject.put("email", email)
            jsonObject.put("admin", false)

            val jsonObjectString = jsonObject.toString()

            val url = URL("http://192.168.1.70/users/register")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
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

            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            if (connection.responseCode == 200) {

                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = Gson().fromJson(inputStreamReader, RegisterResponse::class.java)
                Log.d("A message", response.message)
                updateUI(connection.responseCode, response.message)
                inputStreamReader.close()
                inputSystem.close()
            } else {
                updateUI(connection.responseCode,"Network error")
            }
        }
    }

    private fun updateUI(responseCode: Int, responseMsg: String) {
        runOnUiThread {
            kotlin.run {
                if (responseCode == 200) {
                    Toast.makeText(this, responseMsg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, responseMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
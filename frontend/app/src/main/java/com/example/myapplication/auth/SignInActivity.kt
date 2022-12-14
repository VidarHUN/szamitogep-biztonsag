package com.example.myapplication.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivitySignInBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    companion object{
        var token = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextSignInEmail.text.toString()

            //TODO passwd should be hashed, SQLI
            val pass = binding.editTextSignInPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                sendData(email, pass).start()
            } else {
                Toast.makeText(this, "Make sure to fill every field", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendData(email: String, pass: String): Thread {
            return Thread {
                val jsonObject = JSONObject()

                val basic_user_pass = io.grpc.okhttp.internal.Credentials.basic(email, pass)

                val jsonObjectString = jsonObject.toString()


                val url = URL("http://192.168.1.70/users/login")
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
                connection.setRequestProperty("Authorization", basic_user_pass)

                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(jsonObjectString)
                outputStreamWriter.flush()

                if (connection.responseCode == 200) {
                    val inputSystem = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                    val response = Gson().fromJson(inputStreamReader, Token::class.java)
                    Log.d("A token", response.token)
                    token = response.token
                    updateUI(connection.responseCode)
                    inputStreamReader.close()
                    inputSystem.close()
                }else {
                    updateUI(connection.responseCode)
                }
            }
    }

    private fun updateUI(responseCode: Int) {
        runOnUiThread {
            kotlin.run {
                Log.d("RESPONSE CODE IN IF", responseCode.toString())
                if(responseCode == 200){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                }else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
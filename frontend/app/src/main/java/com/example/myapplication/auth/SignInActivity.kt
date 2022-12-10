package com.example.myapplication.auth

import android.content.Intent
import android.net.Credentials
import android.net.wifi.hotspot2.pps.Credential
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivitySignInBinding
import com.google.cloud.audit.AuthorizationInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.Authenticator
import java.net.HttpURLConnection
import java.net.URL

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextSignInEmail.text.toString()

            //TODO passwd should be hashed, SQLI
            val pass = binding.editTextSignInPassword.text.toString()

            val jsonObject = JSONObject()
            //jsonObject.put("email", "b@c.com")
            //jsonObject.put( "password", "password")
            //jsonObject.put("admin", true)
            //jsonObject.put("name", "name1")
            var responseCode = 0

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            val basic = io.grpc.okhttp.internal.Credentials.basic(email, pass)
            GlobalScope.launch(Dispatchers.IO) {
                val url = URL("http://192.168.1.93/users/login")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
                httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
                httpURLConnection.doInput = true
                httpURLConnection.doOutput = true
                httpURLConnection.setRequestProperty("Authorization", basic)

                val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
                outputStreamWriter.write(jsonObjectString)
                outputStreamWriter.flush()

                responseCode = httpURLConnection.responseCode
                Log.d("RESPONSE CODE", responseCode.toString())
               val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                 withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response))
                    Log.d("Pretty Printed JSON :", prettyJson)

               }

            }

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if(responseCode == 200){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                /*
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                */
            } else {
                Toast.makeText(this, "Make sure to fill every field", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        /*
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        */
    }
}
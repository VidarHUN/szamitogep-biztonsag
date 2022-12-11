package com.example.myapplication.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivitySignInBinding
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    //private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        //firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignUp.setOnClickListener {
            val username = binding.editTextTextPersonName.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()
            val confirmPass = binding.editTextTextConfirmPassword.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put( "password", pass)
            jsonObject.put("name", username)
            jsonObject.put("email", email)
            jsonObject.put("admin", false)


            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            var resstring = ""
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    GlobalScope.launch(Dispatchers.IO) {
                        val url = URL("http://192.168.1.93/users/register")
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

                        if(connection.responseCode == 200){

                            val inputSystem = connection.inputStream
                            val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                            val response = Gson().fromJson(inputStreamReader, RegisterResponse::class.java)
                            Log.d("A message", response.message)
                            resstring = response.message
                            inputStreamReader.close()
                            inputSystem.close()
                        } else {
                           Log.e("ERROR", "Network error")
                        }
                    }



                    //ide jon majd a user creation, ha sikeres, akkor ez a kovi ket sor, ha nem, akkor hiba
                    if (resstring.contentEquals("registered successfully")){
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                    }

                    /*
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val profileUpdates = userProfileChangeRequest {
                                displayName = username
                            }
                            user!!.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User profile updated.")
                                    }
                                }
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                    */
                } else {
                    Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Make sure to fill every field", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
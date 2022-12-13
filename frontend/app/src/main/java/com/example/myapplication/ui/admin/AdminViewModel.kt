package com.example.myapplication.ui.admin

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.ui.admin.adapter.UserItem
import com.example.myapplication.ui.admin.adapter.UserItemAdapter
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AdminViewModel : ViewModel() {
    var usersList: ArrayList<UserItem> = ArrayList()

    fun setUsersList(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        Log.e("userselem", usersList.size.toString())


        getUsersData().start()
        Thread.sleep(1000)
        val userItemAdapter = UserItemAdapter(usersList)
         Log.e("setutan", usersList.size.toString())
        recyclerView.adapter = userItemAdapter
    }

    private fun getUsersData(): Thread {
        return Thread {

            val url = URL("http://192.168.1.93/users/list")
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
                val response = Gson().fromJson(inputStreamReader, UsersList::class.java)
                usersList = response.users
                Log.e("Inthread", usersList.size.toString())
                inputStreamReader.close()
                inputSystem.close()
            } else if (connection.responseCode == 403) {

            }
        }
    }
}

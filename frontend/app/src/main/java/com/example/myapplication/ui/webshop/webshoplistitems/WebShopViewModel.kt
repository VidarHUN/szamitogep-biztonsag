package com.example.myapplication.ui.webshop.webshoplistitems

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.ui.admin.UsersList
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItemAdapter
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WebShopViewModel : ViewModel() {
    var caffList: ArrayList<WebShopItem> = ArrayList()

    fun setCAFFList(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewWebShop)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        getGifs().start()
        Thread.sleep(2000)
        Log.e("elotte", caffList.size.toString())

        val webShopItemAdapter = WebShopItemAdapter(caffList, view.context as FragmentActivity)
        Log.e("utana", caffList.size.toString())
        recyclerView.adapter = webShopItemAdapter
        }

    private fun getGifs(): Thread {
        return Thread {

            val url = URL("http://192.168.1.93/request/list")
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
                val response = Gson().fromJson(inputStreamReader, CaffList::class.java)
                caffList = response.res
                inputStreamReader.close()
                inputSystem.close()
            }
        }
    }
}



package com.example.myapplication.ui.webshop.webshoplistitems

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItemAdapter
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

class WebShopViewModel : ViewModel() {
    var caffList: ArrayList<WebShopItem> = ArrayList()
    val caffArray = arrayOf("0")

    fun sendGet() {
        val url = URL("https://localhost:8443/request/list")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }
        }
    }

    fun setCAFFList(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewWebShop)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Get list of gifs from GET:localhost:8443/requests/list

        // Get gifs by iterating the list what we retreive above
        // GET:localhost:8443/requests/<gif>


        sendGet()


        caffList.add(WebShopItem(0, "Birthday cake", R.drawable.image0))
        caffList.add(WebShopItem(1, "Spongebob", R.drawable.giphy1))
        caffList.add(WebShopItem(1, "Girl with horse", R.drawable.image1))
        caffList.add(WebShopItem(1, "Lorelai Gilmore", R.drawable.giphy12))
        caffList.add(WebShopItem(2, "Sunset", R.drawable.image2))
        caffList.add(WebShopItem(1, "Rory Gilmore", R.drawable.giphy11))
        caffList.add(WebShopItem(3, "Garden", R.drawable.image3))
        caffList.add(WebShopItem(1, "Jess and Luke", R.drawable.giphy10))
        caffList.add(WebShopItem(4, "Cat sitting on fence", R.drawable.image4))
        caffList.add(WebShopItem(1, "Rue", R.drawable.giphy7))
        caffList.add(WebShopItem(5, "Train", R.drawable.image5))
        caffList.add(WebShopItem(1, "Jess and Luke", R.drawable.giphy2))
        caffList.add(WebShopItem(6, "Bookstore", R.drawable.image6))
        caffList.add(WebShopItem(1, "Jess and Luke", R.drawable.giphy3))
        caffList.add(WebShopItem(7, "Fruit market", R.drawable.image7))
        caffList.add(WebShopItem(1, "Jess and Luke", R.drawable.giphy5))
        caffList.add(WebShopItem(8, "Cows on field", R.drawable.image8))
        caffList.add(WebShopItem(9, "People chatting", R.drawable.image9))
        caffList.add(WebShopItem(10, "House", R.drawable.image10))
        caffList.add(WebShopItem(11, "Vending machine", R.drawable.image11))
        caffList.add(WebShopItem(12, "Sunset", R.drawable.image12))
        caffList.add(WebShopItem(13, "Street at night", R.drawable.image13))
        caffList.add(WebShopItem(14, "Netflix", R.drawable.image14))
        caffList.add(WebShopItem(15, "Rory studying", R.drawable.image15))

        /*
        var counter = 0
        for (item in caffList) {
            caffArray[counter++] = item.title
        }
        */

        val webShopItemAdapter = WebShopItemAdapter(caffList)
        recyclerView.adapter = webShopItemAdapter

    }
}
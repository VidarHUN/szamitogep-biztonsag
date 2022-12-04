package com.example.myapplication.ui.webshop.webshoplistitems

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItemAdapter
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

class WebShopViewModel : ViewModel() {
    var caffList: ArrayList<WebShopItem> = ArrayList()

    fun setCAFFList(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewWebShop)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Get list of gifs from GET:localhost:8443/requests/list

        // Get gifs by iterating the list what we retreive above
        // GET:localhost:8443/requests/<gif>

        caffList.add(WebShopItem(0, "Birthday cake", R.drawable.image0))
        caffList.add(WebShopItem(1, "Spongebob", R.drawable.giphy1))
        caffList.add(WebShopItem(2, "Girl with horse", R.drawable.image1))
        caffList.add(WebShopItem(3, "Lorelai Gilmore", R.drawable.giphy12))
        caffList.add(WebShopItem(4, "Sunset", R.drawable.image2))
        caffList.add(WebShopItem(5, "Rory Gilmore", R.drawable.giphy11))
        caffList.add(WebShopItem(6, "Garden", R.drawable.image3))
        caffList.add(WebShopItem(7, "Jess and Luke", R.drawable.giphy10))
        caffList.add(WebShopItem(8, "Cat sitting on fence", R.drawable.image4))
        caffList.add(WebShopItem(9, "Rue", R.drawable.giphy7))
        caffList.add(WebShopItem(10, "Train", R.drawable.image5))
        caffList.add(WebShopItem(11, "Jess and Luke", R.drawable.giphy2))
        caffList.add(WebShopItem(12, "Bookstore", R.drawable.image6))
        caffList.add(WebShopItem(13, "Jess and Luke", R.drawable.giphy3))
        caffList.add(WebShopItem(14, "Fruit market", R.drawable.image7))
        caffList.add(WebShopItem(15, "Jess and Luke", R.drawable.giphy5))
        caffList.add(WebShopItem(16, "Cows on field", R.drawable.image8))
        caffList.add(WebShopItem(17, "People chatting", R.drawable.image9))
        caffList.add(WebShopItem(18, "House", R.drawable.image10))
        caffList.add(WebShopItem(19, "Vending machine", R.drawable.image11))
        caffList.add(WebShopItem(20, "Sunset", R.drawable.image12))
        caffList.add(WebShopItem(21, "Street at night", R.drawable.image13))
        caffList.add(WebShopItem(22, "Netflix", R.drawable.image14))
        caffList.add(WebShopItem(23, "Rory studying", R.drawable.image15))


        var list: ArrayList<String> = ArrayList()
        list.add("https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png")
        list.add("https://i.pinimg.com/564x/64/f5/db/64f5dbb37b5c148f2f6c00485e795559.jpg")

        val db = Firebase.firestore

        val comments = db.collection("CAFFs")

        for(item in caffList){
            comments.document(item.id.toString()).collection("comments")
        }

        val webShopItemAdapter = WebShopItemAdapter(caffList, list)
        recyclerView.adapter = webShopItemAdapter

    }
}
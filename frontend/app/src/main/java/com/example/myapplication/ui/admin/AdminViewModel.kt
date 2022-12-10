package com.example.myapplication.ui.admin

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.admin.adapter.UserItem
import com.example.myapplication.ui.admin.adapter.UserItemAdapter
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItemAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminViewModel : ViewModel() {
    var usersList: ArrayList<UserItem> = ArrayList()

    fun setUsersList(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Get list of gifs from GET:localhost:8443/requests/list

        // Get gifs by iterating the list what we retreive above
        // GET:localhost:8443/requests/<gif>

        usersList.add(UserItem(0, "Joe Doe", "joe.doe@gmail.com"))
        usersList.add(UserItem(1, "Michael Smith", "micheal_smith@gmail.com"))
        usersList.add(UserItem(2, "Marie Potter", "marie.potter@gmail.com"))
        usersList.add(UserItem(3, "Babett Weasley", "babett_weasly@gmail.com"))
        usersList.add(UserItem(4, "Harry Williams", "harry.williams@gmail.com"))
        usersList.add(UserItem(5, "Liam Jones", "liam_jones@gmail.com"))
        usersList.add(UserItem(6, "Olivia Miller", "olivia.miller@gmail.com"))
        usersList.add(UserItem(7, "Charlotte Granger", "charlotte_granger@gmail.com"))
        usersList.add(UserItem(8, "Marcia Johnson", "marcia.johnson@gmail.com"))
        usersList.add(UserItem(9, "Noah Brown", "noah_brown@gmail.com"))

        val userItemAdapter = UserItemAdapter(usersList)
        recyclerView.adapter = userItemAdapter
    }
}
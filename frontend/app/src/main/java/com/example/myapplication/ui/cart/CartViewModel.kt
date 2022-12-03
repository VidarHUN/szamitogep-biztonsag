package com.example.myapplication.ui.cart

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.cart.cartadapter.CartRepo
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem

class CartViewModel : ViewModel() {
    lateinit var cartRepo: CartRepo
    fun addItemToCart(view: View, item: WebShopItem){

    }

    public fun getCart() : LiveData<ArrayList<WebShopItem>> {
        return cartRepo.getCart()
    }

}
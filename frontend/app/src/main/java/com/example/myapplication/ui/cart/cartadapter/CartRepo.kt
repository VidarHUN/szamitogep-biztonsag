package com.example.myapplication.ui.cart.cartadapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem

public class CartRepo {
    lateinit var mutableCart: MutableLiveData<ArrayList<WebShopItem>>

    fun getCart() : LiveData<ArrayList<WebShopItem>> {
        if(mutableCart.value == null){
            initCart()
        }
        return mutableCart
    }

    private fun initCart() {
        mutableCart.value = ArrayList<WebShopItem>()
    }
}
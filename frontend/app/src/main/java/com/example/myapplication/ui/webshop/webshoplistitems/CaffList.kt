package com.example.myapplication.ui.webshop.webshoplistitems

import com.example.myapplication.ui.webshop.webshoplistitems.adapter.WebShopItem
import io.grpc.Status
import org.json.JSONObject

class CaffList (
    var res: ArrayList<WebShopItem>,
    var status: Int,
    var msg: String,
    var no_of_gifs: Int

    )
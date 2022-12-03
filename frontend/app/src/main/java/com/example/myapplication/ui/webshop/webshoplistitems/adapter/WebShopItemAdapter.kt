package com.example.myapplication.ui.webshop.webshoplistitems.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.ui.webshop.webshopitemdetails.WebShopItemDetailsFragment
import com.example.myapplication.ui.webshop.webshopitemdetails.commentadapter.CommentItem
import kotlinx.android.synthetic.main.recycler_item_caff.view.*


class WebShopItemAdapter(
        var webShopItems: ArrayList<WebShopItem>
    ) : RecyclerView.Adapter<WebShopItemAdapter.WebShopViewHolder>() {
    private var context: Context? = null
    private lateinit var view: View

    inner class WebShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewRecyclerItem)
        val textView: TextView = itemView.findViewById(R.id.textViewTitleRecyclerItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebShopViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_caff, parent, false)
        return WebShopViewHolder(view)
    }

    override fun getItemCount(): Int {
        return webShopItems.size
    }

    override fun onBindViewHolder(holder: WebShopViewHolder, position: Int) {
        holder.itemView.textViewTitleRecyclerItem.text = webShopItems[position].title
        val value = TypedValue()
        view.resources.getValue(webShopItems[position].image, value, true)
        if(value.string.takeLast(3) == "jpg" || value.string.takeLast(3) == "png"){
            holder.itemView.imageViewRecyclerItem.setImageResource(webShopItems[position].image)
            holder.itemView.gifImageView.setImageResource(0)
        }
        else if(value.string.takeLast(3) == "gif"){
            holder.itemView.imageViewRecyclerItem.setImageResource(0)
            holder.itemView.gifImageView.setImageResource(webShopItems[position].image)
        }

        holder.itemView.setOnClickListener { v ->
            val activity = v!!.context as AppCompatActivity
            val list: ArrayList<CommentItem> = ArrayList()
            list.add(
                CommentItem(
                    5,
                    "Liam Jones",
                    "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt."
                )
            )
            val webShopItemFragment = WebShopItemDetailsFragment(webShopItems[position].title, webShopItems[position].image, list)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, webShopItemFragment).addToBackStack(null).commit()
        }

        holder.itemView.buttonAddToCart.setOnClickListener { v ->

        }

    }


}




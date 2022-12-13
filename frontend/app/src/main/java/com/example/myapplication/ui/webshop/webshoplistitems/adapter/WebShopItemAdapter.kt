package com.example.myapplication.ui.webshop.webshoplistitems.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.ui.webshop.webshopitemdetails.WebShopItemDetailsFragment
import com.example.myapplication.ui.webshop.webshopitemdetails.commentadapter.CommentItem
import com.example.myapplication.ui.webshop.webshoplistitems.CaffList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.recycler_item_caff.view.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class WebShopItemAdapter (
        var webShopItems: ArrayList<WebShopItem>,
        var activity: FragmentActivity
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
        val imageView = holder.itemView.imageViewRecyclerItem
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null

        executor.execute {

            var url: String = "http://192.168.1.93/request/"+webShopItems[position].image
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {

                var `in` = java.net.URL(url).openStream()

                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        activity.runOnUiThread( Runnable (){
            holder.itemView.gifImageView.setImageBitmap(image)
        })

     /*   if(value.string.takeLast(3) == "jpg" || value.string.takeLast(3) == "png"){
            holder.itemView.imageViewRecyclerItem.setImageResource(webShopItems[position].image)
            holder.itemView.gifImageView.setImageResource(0)
        }
        else if(value.string.takeLast(3) == "gif"){
            holder.itemView.imageViewRecyclerItem.setImageResource(0)
            holder.itemView.gifImageView.setImageResource(webShopItems[position].image)
        }
    */
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

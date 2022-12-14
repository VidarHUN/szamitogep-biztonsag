package com.example.myapplication.ui.webshop.webshopitemdetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentWebshopItemDetailsBinding
import com.example.myapplication.ui.webshop.webshopitemdetails.commentadapter.CommentItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_webshop_item_details.view.*
import kotlinx.android.synthetic.main.recycler_item_caff.view.*
import kotlinx.coroutines.tasks.await
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class WebShopItemDetailsFragment(var title: String, var image: String, var commentsList: ArrayList<CommentItem>) : Fragment() {

    private var _binding: FragmentWebshopItemDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var webShopItemDetailsViewModel: WebShopItemDetailsViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebshopItemDetailsBinding.inflate(inflater, container, false)
        root = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webShopItemDetailsViewModel = ViewModelProvider(this)[WebShopItemDetailsViewModel::class.java]

        val imageView = root.gifImageViewDetails
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var imageBit: Bitmap? = null

        executor.execute {
            val url = URL("http://192.168.1.70/request/"+image)
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server

            connection.setRequestProperty("x-access-tokens", SignInActivity.token)
            connection.doInput = true

            if (connection.responseCode == 200) {

                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                imageBit =BitmapFactory.decodeStream(inputSystem)
                inputStreamReader.close()
                inputSystem.close()
            }

            handler.post{
                imageView.setImageBitmap(imageBit)
            }
            /*
            var url: String = "http://192.168.1.70/request/"+image
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {

                var `in` = java.net.URL(url).openStream()

                imageBit = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(imageBit)
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }*/
        }

        activity?.runOnUiThread( Runnable (){
            val resized = imageBit?.let { Bitmap.createScaledBitmap(it, 200, 134, true) }
            root.gifImageViewDetails.setImageBitmap(resized)
        })

        root.textViewWebShopItemDetailTitle.text = title
        webShopItemDetailsViewModel.setCommentList(root)

        root.buttonPostComment.setOnClickListener{
            //todo post-olni a kommentet
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
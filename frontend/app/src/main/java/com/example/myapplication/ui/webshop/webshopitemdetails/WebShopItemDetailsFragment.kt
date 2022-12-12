package com.example.myapplication.ui.webshop.webshopitemdetails

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentWebshopItemDetailsBinding
import com.example.myapplication.ui.webshop.webshopitemdetails.commentadapter.CommentItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_webshop_item_details.view.*
import kotlinx.coroutines.tasks.await

class WebShopItemDetailsFragment(var title: String, var imageID: Int, var image: Int, var commentsList: ArrayList<CommentItem>) : Fragment() {

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
        val value = TypedValue()

        view.resources.getValue(image, value, true)
        if(value.string.takeLast(3) == "jpg" || value.string.takeLast(3) == "png"){
            root.imageViewWebShopItemDetail.setImageResource(image)
            root.gifImageViewDetails.setImageResource(0)
        }
        else if(value.string.takeLast(3) == "gif"){
            root.imageViewWebShopItemDetail.setImageResource(0)
            root.gifImageViewDetails.setImageResource(image)
        }

        root.textViewWebShopItemDetailTitle.text = title
        webShopItemDetailsViewModel.setCommentList(root)

        val db = Firebase.firestore
        val firebaseAuth = FirebaseAuth.getInstance()



        root.buttonPostComment.setOnClickListener{
            val docData = hashMapOf(
                "userID" to firebaseAuth.currentUser,
                "comment" to "root.editTextWriteAComment.text.toString()"
            )
            try {
                db.collection("commentsAll").document(imageID.toString()).collection("comments").document("f").set(docData)
                    .addOnSuccessListener {
                        println("DocumentSnapshot successfully written!")
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener {
                            e -> Log.w(TAG, "Error writing document", e)
                        println("Error writing document")}
            }catch (e: Exception){
                e.printStackTrace()
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
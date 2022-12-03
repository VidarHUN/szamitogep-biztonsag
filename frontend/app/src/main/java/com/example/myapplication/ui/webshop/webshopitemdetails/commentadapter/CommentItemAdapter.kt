package com.example.myapplication.ui.webshop.webshopitemdetails.commentadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.recycler_item_caff.view.*
import kotlinx.android.synthetic.main.recycler_item_comment.view.*
import kotlin.Int


class CommentItemAdapter(
    var commentItems: ArrayList<CommentItem>
    ) : RecyclerView.Adapter<CommentItemAdapter.CommentViewHolder>() {
    private var context: Context? = null

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUser: TextView = itemView.findViewById(R.id.textViewCommentRecyclerUser)
        val textViewComment: TextView = itemView.findViewById(R.id.textViewCommentRecyclerComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentItems.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.itemView.textViewCommentRecyclerUser.text = commentItems[position].user
        holder.itemView.textViewCommentRecyclerComment.text = commentItems[position].comment
    }

}
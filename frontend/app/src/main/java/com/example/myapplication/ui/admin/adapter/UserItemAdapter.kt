package com.example.myapplication.ui.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.admin.AdminProfileSettingsFragment


class UserItemAdapter (
        var userItems: ArrayList<UserItem>
    ) : RecyclerView.Adapter<UserItemAdapter.UserViewHolder>() {
    private var context: Context? = null
    private lateinit var view: View

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewUsername)
        val buttonEditUser: AppCompatButton = itemView.findViewById(R.id.buttonAdminEditUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userItems.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.textView.text = userItems[position].username

        holder.buttonEditUser.setOnClickListener { v ->
            val activity = v!!.context as AppCompatActivity

            val adminProfileSettingsFragment = AdminProfileSettingsFragment(userItems[position].public_id)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, adminProfileSettingsFragment).addToBackStack(null).commit()
        }
    }
}




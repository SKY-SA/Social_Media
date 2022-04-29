package com.sky.socialmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sky.socialmedia.R
import com.sky.socialmedia.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_recycler_row.view.*

class ProfileRecyclerAdapter(val listPost: ArrayList<Post>) : RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileHolder>() {
    class ProfileHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.profile_recycler_row,parent,false)
        return ProfileHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.itemView.profile_recycler_row_user_comment.text = listPost[position].userComment
        Picasso.get().load(listPost[position].imageUrl).into(holder.itemView.profile_recycler_row_image)
    }

    override fun getItemCount(): Int {
       return listPost.size
    }
    fun UpdateListPost(newListPost : ArrayList<Post>){
        listPost.clear()
        listPost.addAll(newListPost)
        notifyDataSetChanged()
    }
}
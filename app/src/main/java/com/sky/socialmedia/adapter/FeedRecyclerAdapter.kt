package com.sky.socialmedia.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sky.socialmedia.R
import com.sky.socialmedia.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class FeedRecyclerAdapter(val listPost:ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.FeedHolder>(){
    class FeedHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return FeedHolder(view)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.itemView.recycler_row_user_email.text = listPost[position].userEmail
        holder.itemView.recycler_row_user_comment.text = listPost[position].userComment
        Picasso.get().load(listPost[position].imageUrl).into(holder.itemView.recycler_row_image)
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
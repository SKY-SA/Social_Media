package com.sky.socialmedia.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.sky.socialmedia.model.Post

class FeedViewModel : ViewModel() {

    val posts = MutableLiveData<ArrayList<Post>>()
    private var listPost = arrayListOf<Post>()

    fun GetDataDatabase(database :FirebaseFirestore){
        database.collection("Post")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error != null){
                   throw error;
                }
                else if(value != null && !value.isEmpty){
                    val documents = value.documents
                    for(document in documents){
                        val userEmail = document.get("userEmail") as String
                        val userComment = document.get("userComment") as String
                        val imageUrl = document.get("urlImage") as String
                        val downloadPost = Post(userEmail,userComment,imageUrl,)
                        listPost.add(downloadPost)

                    }
                    if(listPost != null) {
                        posts.value = listPost
                    }
                }
            }
    }
}
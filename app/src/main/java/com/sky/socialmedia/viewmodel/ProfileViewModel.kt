package com.sky.socialmedia.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.sky.socialmedia.model.Post

class ProfileViewModel : ViewModel() {

    val posts = MutableLiveData<ArrayList<Post>>()
    private  var listPost = arrayListOf<Post>()


    fun GetData(currentUserEmail:String?, database: FirebaseFirestore){
        if(currentUserEmail != null){
            database.collection("Post").get().addOnSuccessListener { documents->
                for(doc in documents){
                    val downloadUserEmail = doc.get("userEmail") as String
                    if(downloadUserEmail == currentUserEmail ) {
                        val urlImage = doc.get("urlImage") as String
                        val userComment = doc.get("userComment") as String
                        val downloadPost = Post(currentUserEmail, userComment, urlImage)

                        listPost.add(downloadPost)
                    }
                }
                posts.value = listPost
            }.addOnFailureListener{
                throw error("Error SKY")
            }
        }
    }
}
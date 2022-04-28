package com.sky.socialmedia.view

import android.icu.util.Freezable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.sky.socialmedia.R
import com.sky.socialmedia.adapter.ProfileRecyclerAdapter
import com.sky.socialmedia.model.Post
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter: ProfileRecyclerAdapter


    var listPost = ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Sending data from Activities
        val intent = intent
        val userEmailFromActivity = intent.getStringExtra("userEmail")
        println("Gelen email ${userEmailFromActivity}")

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser!!.email.toString()
        profile_acitivity_user_email.text = "${currentUser}"

        GetData()
        for (post in listPost){
            println(post.userComment)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        recyclerViewAdapter = ProfileRecyclerAdapter(listPost)
        recyclerView2.adapter = recyclerViewAdapter

    }

    fun GetData() {
        val currentUserEmail = auth.currentUser!!.email.toString()

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
                    recyclerViewAdapter.notifyDataSetChanged()
                }

            }.addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
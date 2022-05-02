package com.sky.socialmedia.view

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Freezable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.sky.socialmedia.R
import com.sky.socialmedia.adapter.ProfileRecyclerAdapter
import com.sky.socialmedia.model.Post
import com.sky.socialmedia.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter: ProfileRecyclerAdapter
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    var listPost = ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Sending data from Activities
        val intent = intent
        val userEmailFromActivity = intent.getStringExtra("userEmail")
        //println("Gelen email ${userEmailFromActivity}")
        var date: String? = ""
        sharedPreferences = getSharedPreferences("com.sky.socialmedia.view", Context.MODE_PRIVATE)
        sharedPreferences.let {
            date = it.getString("loginedLastDate","")

        }
        if(date != null && !date.isNullOrEmpty()){
            profile_dateTextView.text = date
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser!!.email.toString()
        profile_acitivity_user_email.text = "${currentUser}"

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getData(currentUser,database)
        observeLiveData()


        val layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        recyclerViewAdapter = ProfileRecyclerAdapter(listPost)
        recyclerView2.adapter = recyclerViewAdapter

    }
    fun observeLiveData(){
        profileViewModel.posts.observe(this, Observer{ posts->
            posts?.let {
                recyclerViewAdapter.UpdateListPost(it)
            }

        })
    }

}
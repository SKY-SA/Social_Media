package com.sky.socialmedia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sky.socialmedia.R
import com.sky.socialmedia.adapter.FeedRecyclerAdapter
import com.sky.socialmedia.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_posts.*

class PostsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database :FirebaseFirestore

    private lateinit var feedViewModel:FeedViewModel
    private var recyclerViewAdapter = FeedRecyclerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContentView(R.layout.activity_posts)

        // initializing the firebase variables
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        // This is a Business code that getData From database which is firebase (MVVM)
        feedViewModel.getDataFromDatabase(database)

        observeLiveData()

        val layoutManager = LinearLayoutManager(this)
        postsActivity_recyclerView.layoutManager = layoutManager
        postsActivity_recyclerView.adapter = recyclerViewAdapter

    }
    private fun observeLiveData(){

        feedViewModel.posts.observe(this,Observer{ posts->
            posts?.let {
                recyclerViewAdapter.refreshData(posts)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sharePhoto){
            val intent = Intent(this,SharePhotoActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.logOut){
            // we have to sign out the fireBase
            auth.signOut()
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(item.itemId == R.id.profile){
            val currentUserEmail = auth.currentUser!!.email.toString()
            val intentToProfile = Intent(this, ProfileActivity::class.java)
            // we are sending variable to other activity
            intentToProfile.putExtra("userEmail",currentUserEmail)
            startActivity(intentToProfile)
        }
        if(item.itemId == R.id.showLocation){
            val intent = Intent(this,MapsActivity :: class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
package com.sky.socialmedia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.sky.socialmedia.R
import com.sky.socialmedia.adapter.FeedRecyclerAdapter
import com.sky.socialmedia.model.Post
import com.sky.socialmedia.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var storage :FirebaseStorage
    private lateinit var database :FirebaseFirestore

    private lateinit var feedViewModel:FeedViewModel
    private lateinit var recyclerViewAdapter : FeedRecyclerAdapter
    var listPost = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        feedViewModel.GetDataDatabase(database)
        ObserveLiveData()


        //recyclerViewAdapter.notifyDataSetChanged()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = FeedRecyclerAdapter(listPost)
        recyclerView.adapter = recyclerViewAdapter

    }
    fun ObserveLiveData(){
        feedViewModel.posts.observe(this,Observer{ posts->
            posts?.let {
                recyclerViewAdapter.UpdateListPost(posts)
            }

        })
    }
    fun GetData(){

        database.collection("Post")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
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
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
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
            auth.signOut()
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(item.itemId == R.id.profile){
            val currentUserEmail = auth.currentUser!!.email.toString()
            val intentToProfile = Intent(this, ProfileActivity::class.java)
            println("profile gidiliyor ${currentUserEmail}")
            intentToProfile.putExtra("userEmail",currentUserEmail)
            startActivity(intentToProfile)
        }
        return super.onOptionsItemSelected(item)
    }
}
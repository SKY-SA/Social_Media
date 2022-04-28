package com.sky.socialmedia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sky.socialmedia.R
import kotlinx.android.synthetic.main.activity_userlogin.*

class UserLoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    var userEmail :String? =null
    var userPassword :String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlogin)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intent = Intent(this,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    fun SignIn(view: View){
        userEmail = txtEmail.text.toString()
        userPassword = txtPassword.text.toString()
        if(!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()){
            auth.signInWithEmailAndPassword(userEmail!!,userPassword!!).addOnCompleteListener{
                if(it.isSuccessful){
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    Toast.makeText(this, "Welcome ${currentUserEmail}", Toast.LENGTH_SHORT).show()
                    StartFeedActivity()
                }
            }
        }
    }
    fun StartFeedActivity(){
        val intent = Intent(this,FeedActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun SignUp(view:View){

        userEmail = txtEmail.text.toString()
        userPassword = txtPassword.text.toString()
        if(!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()){
            auth.createUserWithEmailAndPassword(userEmail!!,userPassword!!).addOnCompleteListener{
                if(it.isSuccessful){
                  StartFeedActivity()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
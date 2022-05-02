package com.sky.socialmedia.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sky.socialmedia.R
import kotlinx.android.synthetic.main.activity_userlogin.*
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserLoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    var userEmail :String? =null
    var userPassword :String? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        try
        {
            sharedPreferences = this.getSharedPreferences("com.sky.socialmedia.view", Context.MODE_PRIVATE)
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if(currentUser!=null){
                val intent = Intent(this,PostsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }catch (exception : Exception){
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlogin)
    }


    fun signIn(view: View){

        val date = getDateNow()

       // sharedPreferences.edit().remove("loginedLastDate").apply()
        userEmail = txtEmail.text.toString()
        userPassword = txtPassword.text.toString()
        if(!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()){
            auth.signInWithEmailAndPassword(userEmail!!,userPassword!!).addOnCompleteListener{
                if(it.isSuccessful){
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    Toast.makeText(this, "Welcome ${currentUserEmail}", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().putString("loginedLastDate", date).apply()

                   startFeedActivity()
                }
            }.addOnFailureListener { exception->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDateNow() :String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted.toString()
    }
    fun signUp(view:View){

        userEmail = txtEmail.text.toString()
        userPassword = txtPassword.text.toString()
        if(!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()){
            auth.createUserWithEmailAndPassword(userEmail!!,userPassword!!).addOnCompleteListener{
                if(it.isSuccessful){

                   startFeedActivity()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startFeedActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
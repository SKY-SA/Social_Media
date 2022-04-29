package com.sky.socialmedia.viewmodel

import android.app.Activity
import android.app.ActivityManager
import android.app.Notification
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SharePhotoViewModel : ViewModel() {

    fun Share(activity: Activity,database: FirebaseFirestore,auth:FirebaseAuth,storage: FirebaseStorage, chosenImageUri: Uri?,txtComment:TextView){
        val uuid = UUID.randomUUID()
        val nameImage = "${uuid}.jpg"
        val reference = storage.reference
        var imageReference = reference.child("images").child(nameImage)

        if(chosenImageUri!=null){
            imageReference.putFile(chosenImageUri!!).addOnSuccessListener {

                val referenceUploadedImage = reference.child("images").child(nameImage)
                referenceUploadedImage.downloadUrl.addOnSuccessListener {
                    val urlUploadedImage = it.toString()
                    val userEmail = auth.currentUser!!.email.toString()
                    val userComment = txtComment.text.toString()
                    val date = Timestamp.now()

                    val postHashMap = HashMap<String, Any>()
                    postHashMap.put("urlImage", urlUploadedImage)
                    postHashMap.put("userEmail",userEmail)
                    postHashMap.put("userComment",userComment)
                    postHashMap.put("date",date)

                    // Database operations
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if(it.isSuccessful){
                            return@addOnCompleteListener
                        }
                    }.addOnFailureListener { exception->
                        Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {exception->
                    Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

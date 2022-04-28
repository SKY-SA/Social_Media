package com.sky.socialmedia.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sky.socialmedia.R
import kotlinx.android.synthetic.main.activity_share_photo.*
import java.util.*

class SharePhotoActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    var chosenImageUri: Uri? = null
    var chosenImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_photo)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    fun ChoosePhoto(view:View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // daha önce galeriye giriş için izin alınmamış ise
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            // izin alınmış ise
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
     if(requestCode == 1){
         if(grantResults.size != null && grantResults[0] == PackageManager.PERMISSION_GRANTED)
         {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
         }
     }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            chosenImageUri = data.data
            if(chosenImageUri!=null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,chosenImageUri!!)
                    chosenImageBitmap = ImageDecoder.decodeBitmap(source)
                }else{
                    chosenImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,chosenImageUri!!)
                }

                if(chosenImageBitmap!= null){
                    imageView.setImageBitmap(chosenImageBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun SharePhoto(view:View){
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
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
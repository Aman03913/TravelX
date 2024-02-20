package com.example.travex.post

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.travex.HomeActivity
import com.example.travex.Models.Post
import com.example.travex.Utils.POST
import com.example.travex.Utils.POST_FOLDER
import com.example.travex.Utils.USER_PROFILE_FOLDER
import com.example.travex.Utils.uploadImage
import com.example.travex.databinding.ActivityPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?=null
    private  val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER){
                url->
                if(url!=null){
                     binding.selectImage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener{
            launcher.launch("image/*")
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }
        binding.postBtn.setOnClickListener {
            val post:Post= Post(imageUrl!!,binding.caption.editText?.text.toString())

            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                    startActivity(Intent(this@PostActivity,HomeActivity::class.java))
                    finish()
                }

            }
        }

    }
}
package com.example.travex.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.travex.HomeActivity
import com.example.travex.Models.Reel
import com.example.travex.Utils.POST_FOLDER
import com.example.travex.Utils.REEL
import com.example.travex.Utils.REEL_FOLDER
import com.example.travex.Utils.uploadImage
import com.example.travex.Utils.uploadVideo
import com.example.travex.databinding.ActivityReelsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit  var videoUrl:String
    lateinit var proogressDiaglog:ProgressDialog
    private  val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER,proogressDiaglog){
                    url->
                if(url!=null){

                    videoUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        proogressDiaglog=ProgressDialog(this)

        binding.selectReel.setOnClickListener{
            launcher.launch("video/*")
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
            finish()
        }

        binding.postBtn.setOnClickListener {
            val reel: Reel = Reel(videoUrl!!,binding.caption.editText?.text.toString())

            Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).document().set(reel).addOnSuccessListener {
                    startActivity(Intent(this@ReelsActivity,HomeActivity::class.java))
                    finish()
                }

            }
        }
    }
}
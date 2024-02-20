package com.example.travex

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.travex.Models.User
import com.example.travex.Utils.USER_PROFILE_FOLDER
import com.example.travex.Utils.uploadImage
import com.example.travex.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class SignUp : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    private  val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
          uploadImage(uri, USER_PROFILE_FOLDER){
              if(it==null){

              }else{
                  user.image=it
                  binding.profileImageview.setImageURI(uri)
              }
          }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = "<font color=#FF000000>Already Have an Account</font> <font color=#1E88E5>Click Here ?</font>"
        binding.loginBtn.setText(Html.fromHtml(text))

        user = User()
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){
                binding.signUpBtn.text="Update Profile"
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    Firebase.firestore.collection("User")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener {
                         user=it.toObject<User>()!!
                            if(!user.image.isNullOrEmpty()){
                                Picasso.get().load(user.image).into(binding.profileImageview)
                            }
                            binding.name.editText?.setText(user.name)
                            binding.email.editText?.setText(user.email)
                            binding.password.editText?.setText(user.password)


                        }
                        .addOnFailureListener { exception ->
                            // Handle any errors that occur during the operation
                            // For example, log the error or show a toast message
                        }
                }
            }
        }
        binding.signUpBtn.setOnClickListener {
            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection("User")
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignUp, HomeActivity::class.java))
                            finish()
                        }
                }
            }else {

                if (binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this@SignUp, "Sari Details fill krde bhai", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            Firebase.firestore.collection("User")
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "SignUp Successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this@SignUp, LoginActivity::class.java))

                                }

                        } else {
                            Toast.makeText(
                                applicationContext,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener{
            launcher.launch("image/*")
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this@SignUp,LoginActivity::class.java))

        }
    }
}

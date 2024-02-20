package com.example.travex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.travex.Models.User
import com.example.travex.databinding.ActivityLoginBinding
import com.example.travex.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.logginBtn.setOnClickListener {
            if (binding.email.editText?.text.toString().equals("") or
                binding.password.editText?.text.toString().equals("")
            ) {
                Toast.makeText(this, "Enter your Email id and the password", Toast.LENGTH_LONG)
                    .show()
            } else {
                var user=User(binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString())
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this@LoginActivity, "Login Hogya moj le", Toast.LENGTH_LONG)
                                .show()
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Abe password thik se dal na lodu",Toast.LENGTH_LONG).show()
                        }


                }
            }


        }

    }
}
package com.example.travex.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.travex.Models.User
import com.example.travex.SignUp
import com.example.travex.adapter.ViewPagerAdapterc
import com.example.travex.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private  lateinit var viewPagerAdapter: ViewPagerAdapterc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener {
            val intent=Intent(activity,SignUp::class.java)
            intent.putExtra("MODE",1)
            activity?.startActivity(intent)
            activity?.finish()
        }
        viewPagerAdapter=ViewPagerAdapterc(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(),"My Post")
        viewPagerAdapter.addFragments(MyReelFragment(),"My Reels")

        binding.viewPager.adapter=viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()

        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            Firebase.firestore.collection("User")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    if (user != null) {
                        // User document exists, update UI with user data
                        binding.name.text = user.name
                        binding.bio.text = user.email
                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImageview)
                        }
                    } else {

                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occur during the operation
                    // For example, log the error or show a toast message
                }
        } else {
            // currentUser is null, handle this case if needed
        }
    }

}
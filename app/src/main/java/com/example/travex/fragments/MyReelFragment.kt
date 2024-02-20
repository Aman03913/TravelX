package com.example.travex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.travex.Models.Post
import com.example.travex.Models.Reel
import com.example.travex.R
import com.example.travex.Utils.REEL
import com.example.travex.adapter.MyPostRvAdapter
import com.example.travex.adapter.MyReelAdapter
import com.example.travex.databinding.FragmentMyReelBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class MyReelFragment : Fragment() {
    private lateinit var binding:FragmentMyReelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=FragmentMyReelBinding.inflate(inflater, container, false)
        var reelList=ArrayList<Reel>()
        var adapter= MyReelAdapter(requireContext(),reelList)
        binding.rv.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter=adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+REEL).get().addOnSuccessListener {
            var tempList= arrayListOf<Reel>()
            for(i in it.documents){
                var reel: Reel =i.toObject<Reel>()!!
                tempList.add(reel)
            }
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}
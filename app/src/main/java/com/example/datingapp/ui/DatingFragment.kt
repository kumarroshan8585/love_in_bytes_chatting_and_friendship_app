package com.example.datingapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide.init
import com.example.datingapp.R
import com.example.datingapp.adapter.DatingAdapter
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDatingBinding.inflate(inflater, container, false)

        // Initialize the list to avoid null pointer exceptions
        list = ArrayList()

        // Fetch data
        getData()

        return binding.root
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                // Implement if needed
            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list!!.size) {
                    Toast.makeText(requireContext(), "This is the last card.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
                // Implement if needed
            }

            override fun onCardCanceled() {
                // Implement if needed
            }

            override fun onCardAppeared(view: View?, position: Int) {
                // Implement if needed
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                // Implement if needed
            }
        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }

    companion object{  // Taki iss list ko ham kisi or jagah bhi use kar sake
        var list: ArrayList<UserModel>?=null
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Roshan", "onDataChange: ${snapshot.toString()}")
                    if (snapshot.exists()) {
//                        list!!.clear() // Clear the list before adding new data
                        list= arrayListOf()
                        for (data in snapshot.children) {
                            val model = data.getValue(UserModel::class.java)
                            if(model!!.number!=FirebaseAuth.getInstance().currentUser!!.phoneNumber){
                                list!!.add(model!!)
                            }

                        }
                        list!!.shuffle() // Shuffle the list

                        // Initialize CardStack components
                        init()
                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()
                        binding.cardStackView.adapter = DatingAdapter(requireContext(), list!!)
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}

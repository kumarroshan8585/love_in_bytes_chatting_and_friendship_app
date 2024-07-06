package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.activity.MessageActivity
import com.example.datingapp.databinding.ItemUserLayoutBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.ui.VideoCall


//Recycler view best explained by https://www.youtube.com/watch?v=5pqbODhq-p0&t=201s
//class DatingAdapter(val context: Context, val list: ArrayList<UserModel>): RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
//    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
//        :RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
//
//
//        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context)
//        ,parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
//        holder.binding.textView5.text=list[position].name
//        holder.binding.textView4.text=list[position].email
//
//        Glide.with(context).load(list[position].image).into(holder.binding.userImage)
//
//        holder.binding.chat.setOnClickListener{
//            val inten= Intent(context, MessageActivity::class.java)
//            inten.putExtra("userId", list[position].number)
//            Log.d("RoshanChat", "list[position].number: ${list[position].number}")
//            //The code puts an extra piece of data into the Intent using putExtra().
//        // This extra data is a user ID, which is retrieved from the list at the current position (list[position].number).
//
//            context.startActivity(inten)
//        }
//
//        holder.binding.videoCall.setOnClickListener {
//            val intent = Intent(context, VideoCall::class.java)
//            context.startActivity(intent)
//        }
//
//    }
//}

class DatingAdapter(val context: Context, val list: ArrayList<UserModel>): RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.textView5.text = list[position].name
        holder.binding.textView4.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

        holder.binding.chat.setOnClickListener{
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", list[position].number)
            context.startActivity(intent)
        }

        holder.binding.videoCall.setOnClickListener {
            val intent = Intent(context, VideoCall::class.java).apply {
                putExtra("NUMBER", list[position].number) // Or use any relevant data from UserModel
                putExtra("NAME", list[position].name)
            }
            context.startActivity(intent)
        }
    }
}

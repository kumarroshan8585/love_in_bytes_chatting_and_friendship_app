package com.example.datingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.model.MessageModel
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter(val context: Context, val list: List<MessageModel>)
    :RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
//Two constants, MSG_TYPE_RIGHT and MSG_TYPE_LEFT, are defined to
        // distinguish between messages sent by the current user and messages received from others.
    val MSG_TYPE_RIGHT=0
    val MSG_TYPE_LEFT=1




    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){   //It holds references to the TextView and ImageView in the item layout,
        // which are used to display the message text and the sender's image.

        val text=itemView.findViewById<TextView>(R.id.messageText)
        val image=itemView.findViewById<ImageView>(R.id.senderImage)
    }

    override fun getItemViewType(position: Int): Int {  //returns the view type for the item at the given position.
        return if(list[position].senderId==FirebaseAuth.getInstance().currentUser!!.phoneNumber){  //It checks if the senderId of the message matches the current user's phone number
            // (indicating the message was sent by the current user) and
            // returns the corresponding constant (MSG_TYPE_RIGHT or MSG_TYPE_LEFT).
            MSG_TYPE_RIGHT
        }else{
            MSG_TYPE_LEFT
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {  //onCreateViewHolder creates and returns a MessageViewHolder instance.

        return if(viewType==MSG_TYPE_RIGHT){  //It inflates the appropriate layout (layout_receiver_message or layout_sender_message)
            // based on the view type determined by getItemViewType.
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_receiver_message, parent, false))
        }else{
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    //onBindViewHolder binds the data to the views for each item.
    //It sets the message text for the TextView.
    //It fetches the sender's image from Firebase Realtime Database based on the senderId and uses Glide to load the image into the ImageView.
    //If the data is successfully fetched, it sets the image; otherwise, it shows a placeholder image.
    //If the database query is cancelled or fails, it displays a Toast message with the error.
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.text.text=list[position].message

        FirebaseDatabase.getInstance().getReference("users")  //Current user ka number mil jayega
            .child(list[position].senderId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            val data=snapshot.getValue(UserModel::class.java)

                            Glide.with(context).load(data!!.image).placeholder(R.drawable.person).into(holder.image)


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }
}
package com.phoenix.socialmedia.messages

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.data.Messages
import com.phoenix.socialmedia.databinding.MessageRecyclerViewBinding

class MessageAdapter (private val messageList: ArrayList<Messages>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    // using firebase auth
    private val auth = Firebase.auth

    lateinit var binding : MessageRecyclerViewBinding
    inner class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val messageContent = binding.messageText
        val messageBox = binding.messageCard

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        binding = MessageRecyclerViewBinding.inflate(LayoutInflater.from(parent.context))
        return  MessageViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(currentMessage.emailOfMessenger == auth.currentUser?.email.toString()){
            holder.messageBox.setBackgroundColor(Color.BLUE)
            holder.messageContent.setTextColor(Color.WHITE)

        }
        else{
            holder.messageBox.setBackgroundColor(Color.RED)
            holder.messageContent.setTextColor(Color.WHITE)

        }
        holder.messageContent.text = currentMessage.messageContent

    }
}
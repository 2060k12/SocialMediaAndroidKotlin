package com.phoenix.socialmedia.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.data.Messages
import com.phoenix.socialmedia.databinding.MessageRecyclerViewBinding

class MessageAdapter (private val messageList: ArrayList<Messages>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    lateinit var binding : MessageRecyclerViewBinding
    inner class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val messageContent = binding.messageText
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
        holder.messageContent.text = currentMessage.messageContent
    }
}
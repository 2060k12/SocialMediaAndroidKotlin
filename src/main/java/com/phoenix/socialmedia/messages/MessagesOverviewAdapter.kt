package com.phoenix.socialmedia.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.databinding.MessageOverviewRecyclerViewBinding
import com.phoenix.socialmedia.homepage.HomePageViewModel
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class MessagesOverviewAdapter(private val messagesSendersList : ArrayList<String>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<MessagesOverviewAdapter.MessagesOverviewViewHolder>(){
    private lateinit var binding : MessageOverviewRecyclerViewBinding
    private val viewModel = HomePageViewModel()

    // View holder for our adapter
    inner class MessagesOverviewViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val profileImage = binding.messageOfImageView
        val userName = binding.messageOfUserName

        init {
            itemView.setOnClickListener(){
                onItemClickListener.onItemClick(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesOverviewViewHolder {
        binding = MessageOverviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessagesOverviewViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return messagesSendersList.size
    }

    override fun onBindViewHolder(holder: MessagesOverviewViewHolder, position: Int) {
        val currentItem = messagesSendersList[position]

//        get the username from email
        viewModel.getUserProfileImage(currentItem) {
                image, userName ->
            if(image.isNotEmpty()) Picasso.get().load(image).into(holder.profileImage)
            holder.userName.text = userName
        }

    }

}
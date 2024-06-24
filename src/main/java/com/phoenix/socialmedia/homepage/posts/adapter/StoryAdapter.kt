package com.phoenix.socialmedia.homepage.posts.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.StoryRecyclerViewBinding
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class StoryAdapter(private val storyList: ArrayList<Story>, private var itemCLick: OnItemClickListener) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private lateinit var binding : StoryRecyclerViewBinding

    private val auth = Firebase.auth
    inner class StoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val storyImage = binding.storyImageView
        val storyCardView = binding.storyCardView
        init {
            itemView.setOnClickListener{
                val position = layoutPosition
                itemCLick.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
       binding = StoryRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val currentItem = storyList[position]
        Picasso.get().load(currentItem.imageUrl).resize(200,200).centerCrop().into(holder.storyImage)
        if(currentItem.email == auth.currentUser?.email.toString() ){
            holder.storyCardView.setCardBackgroundColor(Color.RED)
        }
    }

}
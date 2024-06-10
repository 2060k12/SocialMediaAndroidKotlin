package com.phoenix.socialmedia.homepage.posts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.StoryRecyclerViewBinding
import com.squareup.picasso.Picasso

class StoryAdapter(private val storyList: ArrayList<Story>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private lateinit var binding : StoryRecyclerViewBinding
    inner class StoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val storyImage = binding.storyImageView

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
    }
}
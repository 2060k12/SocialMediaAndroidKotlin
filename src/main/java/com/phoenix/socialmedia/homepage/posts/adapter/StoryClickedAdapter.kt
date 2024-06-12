package com.phoenix.socialmedia.homepage.posts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.StoryClickedRecyclerViewBinding
import com.squareup.picasso.Picasso

class StoryClickedAdapter(private val storyList: ArrayList<Story>): RecyclerView.Adapter<StoryClickedAdapter.StoryClickedViewHolder>() {

    private lateinit var binding: StoryClickedRecyclerViewBinding
    inner class StoryClickedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

            val storyImage = binding.storyLargeImageView
            val userName = binding.storyUserNameTextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryClickedViewHolder {
            binding = StoryClickedRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StoryClickedViewHolder(binding.root)

        }

        override fun getItemCount(): Int {
            return storyList.size
        }

        override fun onBindViewHolder(holder: StoryClickedViewHolder, position: Int) {
            val currentItem = storyList[position]
            Picasso.get().load(currentItem.imageUrl).rotate(90f).into(holder.storyImage)
            holder.userName.text = currentItem.email
        }
    }

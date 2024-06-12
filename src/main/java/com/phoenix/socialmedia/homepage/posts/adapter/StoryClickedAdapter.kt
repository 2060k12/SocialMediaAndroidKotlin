package com.phoenix.socialmedia.homepage.posts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.StoryClickedRecyclerViewBinding
import com.phoenix.socialmedia.homepage.HomePageViewModel
import com.squareup.picasso.Picasso

class StoryClickedAdapter(private val storyList: ArrayList<Story>): RecyclerView.Adapter<StoryClickedAdapter.StoryClickedViewHolder>() {

    private lateinit var binding: StoryClickedRecyclerViewBinding
    inner class StoryClickedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

            val storyImage = binding.storyLargeImageView
            val userName = binding.storyUserNameTextView
            val userImage = binding.storyUserImageView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryClickedViewHolder {
            binding = StoryClickedRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StoryClickedViewHolder(binding.root)

        }

        override fun getItemCount(): Int {
            return storyList.size
        }

        override fun onBindViewHolder(holder: StoryClickedViewHolder, position: Int) {
            val homePageViewModel = HomePageViewModel()
            val currentItem = storyList[position]
            Picasso.get().load(currentItem.imageUrl).rotate(90f).into(holder.storyImage)
            homePageViewModel.getUserProfileImage(currentItem.email){
                userImage, userName ->
                holder.userName.text = userName
                Picasso.get().load(userImage).placeholder(R.drawable.account).resize(200,200).centerCrop().into(holder.userImage)
            }

        }
    }

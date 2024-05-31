package com.phoenix.socialmedia.homepage.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.PostsRecyclerViewBinding
import com.squareup.picasso.Picasso

class PostAdapter (private val postList: ArrayList<Post> ) :RecyclerView.Adapter<PostAdapter.PostViewHolder>(){
    lateinit var binding: PostsRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        binding = PostsRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
      holder.userNameTextView.setText(currentItem.username)
        holder.captionTextView.setText(currentItem.caption)
        Picasso.get().load(currentItem.imageUrl).resize(500,500).centerCrop().into(holder.postImageView)
        holder.dateTimeTextView.setText(currentItem.uploadDateTimestamp?: null)

    }



    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userNameTextView : TextView = itemView.findViewById(R.id.userNameTextView)
        val captionTextView : TextView = itemView.findViewById(R.id.captionTextView)
        val userProfileImage : ImageView = itemView.findViewById(R.id.userProfileImageView)
        val postImageView : ImageView = itemView.findViewById(R.id.postImageView)
        val dateTimeTextView : TextView = itemView.findViewById(R.id.dateTimeTextView)

    }
}
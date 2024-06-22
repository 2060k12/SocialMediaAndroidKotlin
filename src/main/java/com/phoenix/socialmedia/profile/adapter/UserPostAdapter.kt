package com.phoenix.socialmedia.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.UserProfileRecyclerViewBinding
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class UserPostAdapter(private var post: ArrayList<Post>, private var itemCLick: OnItemClickListener)  :RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder>() {

    lateinit var binding: UserProfileRecyclerViewBinding
    private lateinit var mAdview: AdView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {

        binding = UserProfileRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return UserPostViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {

        val currentItem = post[position]

        if(currentItem.imageUrl.isNotEmpty()){
            Picasso.get().load(currentItem.imageUrl).resize(500,500).centerCrop().into(holder.userUploadedImageView)
        }

    }

    override fun getItemCount(): Int {
        return post.size
    }

   inner class UserPostViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val userUploadedImageView :ImageView = itemView.findViewById(R.id.userUploadedImageView)

        init {
            itemView.setOnClickListener{
                v: View ->
                val position: Int = layoutPosition
                itemCLick.onItemClick(position)
            }
        }
    }
}
package com.phoenix.socialmedia.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.FollowingUsersRecyclerViewBinding
import com.phoenix.socialmedia.profile.ProfileViewModel
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class FollowingListAdapter(private var followingList : ArrayList<Profile>, private val listName: String, private val isCurrentUser: Boolean, private val itemClick : OnItemClickListener) : RecyclerView.Adapter<FollowingListAdapter.FollowingListViewHolder>() {
    private lateinit var binding : FollowingUsersRecyclerViewBinding
    private val profileViewMode = ProfileViewModel()

    inner class FollowingListViewHolder(itemView :View): RecyclerView.ViewHolder(itemView){

        val uerProfileImage : ImageView = itemView.findViewById(R.id.followingUserProfileImageView)
        val followingUserName : TextView = itemView.findViewById(R.id.followingUserNameTextView)
        val removeFollowingButton: Button = itemView.findViewById(R.id.removeFollowingButton)

        init {
            itemView.setOnClickListener(){
                itemClick.onItemClick(layoutPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingListViewHolder {
        binding = FollowingUsersRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent, false )

        return FollowingListViewHolder(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FollowingListViewHolder, position: Int) {
        if(!isCurrentUser){
            binding.removeFollowingButton.visibility = View.GONE
        }
        else{
            binding.removeFollowingButton.visibility = View.VISIBLE

        }

        if(listName == "following"){
            holder.removeFollowingButton.text = "Following"
        }
        else{
            holder.removeFollowingButton.text = "Remove"

        }
        val currentItem = followingList[position]
        holder.followingUserName.text = currentItem.username
        if(currentItem.userImageUrl.isNotEmpty()) {
            Picasso.get().load(currentItem.userImageUrl).resize(200, 200).centerCrop()
                .into(holder.uerProfileImage)
        }
            holder.removeFollowingButton. setOnClickListener {
                if (holder.removeFollowingButton.text.toString().lowercase() == "following" ){
                    profileViewMode.removeFollowing(currentItem.email)
                    holder.removeFollowingButton.text = "Follow"

                }
                else if(holder.removeFollowingButton.text.toString().lowercase() == "follow"){
                    profileViewMode.followUser(currentItem.email)
                    holder.removeFollowingButton.text = "Following"
                }
                else if( holder.removeFollowingButton.text.toString().lowercase() =="remove"){
                    profileViewMode.removeFollwers(currentItem.email)
                    holder.removeFollowingButton.text = "Successfully Removed"

                    followingList.remove(currentItem)
                    holder.removeFollowingButton.isEnabled = false
                }

        }


    }

    override fun getItemCount(): Int {
        return followingList.size
    }
}
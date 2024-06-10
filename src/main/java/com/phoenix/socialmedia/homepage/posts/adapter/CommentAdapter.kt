package com.phoenix.socialmedia.homepage.posts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Comments
import com.phoenix.socialmedia.databinding.CommentsRecyclerViewBinding

class CommentAdapter(private val commentList: ArrayList<Comments> ) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var binding: CommentsRecyclerViewBinding

    inner class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val commentUserProfileImageView : ImageView = itemView.findViewById(R.id.commentUserProfileImage)
        var commentText : TextView = itemView.findViewById(R.id.commentTextView)
        val userName :TextView = itemView.findViewById(R.id.commentUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        binding = CommentsRecyclerViewBinding.inflate(LayoutInflater.from(parent.context))
        return  CommentViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
       return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.userName.text = currentItem.email
        holder.commentText.text = currentItem.comment

    }
}
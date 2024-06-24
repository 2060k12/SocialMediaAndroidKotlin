package com.phoenix.socialmedia.homepage.posts.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.PostsRecyclerViewBinding
import com.phoenix.socialmedia.homepage.HomePageViewModel
import com.phoenix.socialmedia.homepage.posts.comment.CommentViewModel
import com.squareup.picasso.Picasso

class PostAdapter (private val postList: ArrayList<Post>, private val navController: NavController ) :RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    lateinit var binding: PostsRecyclerViewBinding
    private val viewModel: HomePageViewModel = HomePageViewModel()
    private val commentViewModel: CommentViewModel = CommentViewModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        binding =
            PostsRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
            viewModel.isPostLiked(currentItem.postId, currentItem.email){
             state ->
                if (state){
                    currentItem.likedByCurrentUser = true
                    holder.likeButton.setImageResource(R.drawable.heart)
                }
                else {
                    currentItem.likedByCurrentUser = false
                    holder.likeButton.setImageResource(R.drawable.heart_empty)
                }

            }

            holder.dateTimeTextView.text = viewModel.getTimeDifference(currentItem.time.toDate().time)
            binding.adCardView.visibility = View.GONE
            binding.postCardView.visibility = View.VISIBLE
            holder.captionTextView.text = currentItem.caption
            viewModel.getUserProfileImage(currentItem.email) {
                image, userName ->
                if(image.isNotEmpty()) Picasso.get().load(image).into(holder.userProfileImage)
                holder.userNameTextView.text = userName

            }
        if(currentItem.caption.isNotEmpty()){
            holder.captionTextView.visibility = View.VISIBLE
        }

        if (currentItem.imageUrl.isNotEmpty()) {
            Picasso.get().load(currentItem.imageUrl).resize(500, 500).centerCrop()
                .into(holder.postImageView)
        }


            holder.likeButton.setOnClickListener {
                if(currentItem.likedByCurrentUser){
                    holder.likeButton.setImageResource(R.drawable.heart_empty)
                    commentViewModel.deleteLike(email = currentItem.email, postId = currentItem.postId)

                    currentItem.likedByCurrentUser = false
                }
                else{
                    holder.likeButton.setImageResource(R.drawable.heart)
                    commentViewModel.likeThePost(email = currentItem.email, postId = currentItem.postId)
                    currentItem.likedByCurrentUser = true
                }
        }

        holder.addCommentButton.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("postInfo", currentItem)
            navController.navigate(R.id.action_homePageFragment_to_commentFragment, bundle)
//            navController.navigate(R.id.action_userProfileFragment_to_commentFragment, bundle)
        }

    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userNameTextView : TextView = itemView.findViewById(R.id.userNameTextView)
        val captionTextView : TextView = itemView.findViewById(R.id.captionTextView)
        val userProfileImage : ImageView = itemView.findViewById(R.id.userProfileImageView)
        val postImageView : ImageView = itemView.findViewById(R.id.postImageView)
        val dateTimeTextView : TextView = itemView.findViewById(R.id.dateTimeTextView)

        val likeButton: ImageButton = itemView.findViewById(R.id.likePostButton)
        val addCommentButton : ImageButton = itemView.findViewById(R.id.addCommentButton)

    }

}
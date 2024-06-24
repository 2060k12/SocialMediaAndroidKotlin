package com.phoenix.socialmedia.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.ProfilePostViewFragmentBinding
import com.phoenix.socialmedia.homepage.HomePageViewModel
import com.phoenix.socialmedia.homepage.posts.comment.CommentViewModel
import com.squareup.picasso.Picasso

class ProfilePostViewFragment : Fragment() {
    private val viewModel: HomePageViewModel = HomePageViewModel()
    private val commentViewModel: CommentViewModel = CommentViewModel()

    private val auth = Firebase.auth


    lateinit var binding: ProfilePostViewFragmentBinding
 val post @RequiresApi(Build.VERSION_CODES.TIRAMISU)
 get() =  arguments?.getParcelable("postInformation", Post::class.java)
lateinit var mainActivity: MainActivity
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("", R.drawable.add, showBarState = false, false)
        mainActivity.getNavigationBar().visibility = View.GONE
        viewModel.isPostLiked(post?.postId!!, post?.email!!){
                state ->
            if (state){
                post?.likedByCurrentUser = true
                binding.likeClickedPostButton.setImageResource(R.drawable.heart)
            }
            else {
                post?.likedByCurrentUser = false
                binding.likeClickedPostButton.setImageResource(R.drawable.heart_empty)
            }

        }
        // Inflate the layout for this fragment
        binding = ProfilePostViewFragmentBinding.inflate(inflater, container, false)
        binding.postViewProgressView.visibility = View.VISIBLE
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(post?.imageUrl.toString()).rotate(90f).into(binding.clickedPostImageView)
        if(post?.caption.toString().isNotEmpty()){
            binding.commentClickedPostView.text = post?.caption.toString()
        }
        else{
            binding.commentClickedPostView.visibility = View.GONE
        }
        binding.postViewProgressView.visibility = View.GONE


        if(post?.caption?.isEmpty() == true){
            binding.commentClickedPostView.visibility = View.GONE
        }
        else{
            binding.commentClickedPostView.visibility = View.VISIBLE

        }

        binding.commentClickPostButton.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("postInfo", post)
            findNavController().navigate(R.id.action_profilePostViewFragment_to_commentFragment, bundle)

        }
        binding.likeClickedPostButton.setOnClickListener {
            if (post?.likedByCurrentUser!!) {
                binding.likeClickedPostButton.setImageResource(R.drawable.heart_empty)
                commentViewModel.deleteLike(email = post?.email!!, postId = post?.postId!!)

                post?.likedByCurrentUser = false
            } else {
                binding.likeClickedPostButton.setImageResource(R.drawable.heart)
                commentViewModel.likeThePost(email = post?.email!!, postId = post?.postId!!)
                post?.likedByCurrentUser = true
            }

        }

        if(auth.currentUser?.email.toString() == post?.email){
            binding.deleteButton.visibility = View.VISIBLE
        }
        else{
            binding.deleteButton.visibility = View.GONE
        }

        // this will delete the current post
        binding.deleteButton.setOnClickListener{
            viewModel.deletePost(post?.postId, post?.email, requireContext())
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }
}
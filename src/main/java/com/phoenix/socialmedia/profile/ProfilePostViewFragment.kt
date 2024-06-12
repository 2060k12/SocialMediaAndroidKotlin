package com.phoenix.socialmedia.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.ProfilePostViewFragmentBinding
import com.squareup.picasso.Picasso

class ProfilePostViewFragment : Fragment() {

 lateinit var binding: ProfilePostViewFragmentBinding
 val post @RequiresApi(Build.VERSION_CODES.TIRAMISU)
 get() =  arguments?.getParcelable("postInformation", Post::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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




    }
}
package com.phoenix.socialmedia.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.ProfilePostViewFragmentBinding
import com.squareup.picasso.Picasso

class ProfilePostViewFragment : Fragment() {

 lateinit var binding: ProfilePostViewFragmentBinding
 val post @RequiresApi(Build.VERSION_CODES.TIRAMISU)
 get() =  arguments?.getParcelable("postInformation", Post::class.java)
lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("", R.drawable.add, showBarState = false, false)
        mainActivity.getNavigationBar().visibility = View.GONE

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

        binding.commentClickPostButton.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("postInfo", post)
            findNavController().navigate(R.id.action_profilePostViewFragment_to_commentFragment, bundle)

        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }
}
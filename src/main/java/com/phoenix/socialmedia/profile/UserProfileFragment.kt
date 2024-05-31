package com.phoenix.socialmedia.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.ProfileFragmentBinding
import com.phoenix.socialmedia.profile.adapter.UserPostAdapter
import com.squareup.picasso.Picasso

class UserProfileFragment : Fragment() {
    // adding binding of our Fragment
    lateinit var binding : ProfileFragmentBinding

    // this variable stores hthe array list of the post
    private var imageUrls = ArrayList<Post>()
    val userInfo: Profile?
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() {
            val profile = arguments?.getParcelable("profile_info", Profile::class.java)
           return  profile
        }

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    // View Model
    private val viewModel: ProfileViewModel by viewModels()
    // Using firebase authentication
    val auth = Firebase.auth



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userProfile = viewModel.profile.value
        viewModel.getCurrentProfileDetails(auth.currentUser?.email.toString())

            Picasso.get().load(userProfile?.userImageUrl).resize(200,200).centerCrop().into(binding.profileImageView)
            binding.fullNameTextView.setText(userProfile?.name?: "")
            binding.userProfileCaption.setText(userProfile?.userCaption?: "")

    viewModel.profile.observe(viewLifecycleOwner){
    it->
    if(it.userImageUrl.isNotEmpty()){
        Picasso.get().load(it.userImageUrl).resize(200,200).centerCrop().into(binding.profileImageView)
        binding.fullNameTextView.text = it?.name?: ""
        binding.userProfileCaption.text = it?.userCaption?: ""

    }
}

        //Working with the recycler view
        val recyclerView : RecyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter =  UserPostAdapter(imageUrls)



        // Observing the live data, for any changes
        viewModel.imageUrls.observe(viewLifecycleOwner){
            post->
            imageUrls.clear()
            imageUrls.addAll(post)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        val currentUser = auth.currentUser?.email.toString()

        viewModel.getUserAddedImages(currentUser)

        // Get followings
        viewModel.getFollowing(currentUser)
        binding.followingCountText.text = viewModel.followingCount.value.toString()

        // observer for the followings
        viewModel.followingCount.observe(viewLifecycleOwner){
            viewModel.getFollowing(currentUser)
            binding.followingCountText.text = viewModel.followingCount.value.toString()
        }


        // Get Followers
        viewModel.getFollowers(currentUser)
        binding.followingCountText.text = viewModel.followingCount.value.toString()


        viewModel.followersCount.observe(viewLifecycleOwner){
            viewModel.getFollowers(currentUser)
            binding.followersCountText.text = viewModel.followersCount.value.toString()
        }


    }
}
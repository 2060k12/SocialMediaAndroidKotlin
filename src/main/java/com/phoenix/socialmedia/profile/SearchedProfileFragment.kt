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
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.SearchedProfileFragmentBinding
import com.phoenix.socialmedia.profile.adapter.UserPostAdapter
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso


class SearchedProfileFragment : Fragment(), OnItemClickListener {
    lateinit var binding : SearchedProfileFragmentBinding
    private var imageUrls = ArrayList<Post>()

    val userInfo: Profile?
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() {
            val profile = arguments?.getParcelable("profile_info", Profile::class.java)
           return  profile
        }

    companion object {
        fun newInstance() = SearchedProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        val mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar(userInfo?.username.toString(), R.drawable.add, showBarState = true, true)

        binding = SearchedProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userProfile = viewModel.profile.value
        if (userInfo != null) {
            viewModel.getCurrentProfileDetails(userInfo!!.email)
        }

            Picasso.get().load(userProfile?.userImageUrl).resize(200,200).centerCrop().into(binding.profileImageView)
            binding.fullNameTextView.setText(userProfile?.name?: "")
            binding.userProfileCaption.setText(userProfile?.userCaption?: "")

    viewModel.profile.observe(viewLifecycleOwner){
    it->
    if(it.userImageUrl.isNotEmpty()){
        Picasso.get().load(it.userImageUrl?: "").resize(200,200).centerCrop().into(binding.profileImageView)
        binding.fullNameTextView.text = it?.name?: ""
        binding.userProfileCaption.text = it?.userCaption?: ""

    }
}

        //Working with the recycler view
        val recyclerView : RecyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter =  UserPostAdapter(imageUrls, this )



        viewModel.imageUrls.observe(viewLifecycleOwner){
            post->
            imageUrls.clear()
            imageUrls.addAll(post)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        // Get the posts from the user Profile
        viewModel.getUserAddedImages(userInfo!!.email)

        // Follows the user
        binding.followButton.setOnClickListener(){
            viewModel.followUser(userInfo!!.email)
        }
        // Get followings

        viewModel.getFollowing(userInfo!!.email)
        binding.followingCountText.text = viewModel.followingCount.value.toString()

        // observer for the followings
        viewModel.followingCount.observe(viewLifecycleOwner){
            viewModel.getFollowing(userInfo!!.email)
            binding.followingCountText.text = viewModel.followingCount.value.toString()
        }


        // Get Followers
        viewModel.getFollowers(userInfo!!.email)
        binding.followingCountText.text = viewModel.followingCount.value.toString()


        viewModel.followersCount.observe(viewLifecycleOwner){
            viewModel.getFollowers(userInfo!!.email)
            binding.followersCountText.text = viewModel.followersCount.value.toString()
        }





    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}
package com.phoenix.socialmedia.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.ProfileFragmentBinding
import com.phoenix.socialmedia.profile.adapter.UserPostAdapter
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class UserProfileFragment : Fragment(), OnItemClickListener {
    // adding binding of our Fragment
    lateinit var binding : ProfileFragmentBinding
    private var followList = ArrayList<Profile>()



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

        val userProfile = viewModel.profile.value
        // This function will get load the livedata
        viewModel.getCurrentProfileDetails(auth.currentUser?.email.toString())

        Picasso.get().load(userProfile?.userImageUrl).resize(200,200).centerCrop().into(binding.profileImageView)
        binding.fullNameTextView.text = userProfile?.name?: ""
        binding.userProfileCaption.text = userProfile?.userCaption?: ""
        binding.userNameText.text = userProfile?.username?: ""
    viewModel.profile.observe(viewLifecycleOwner){
    it->
    if(it.userImageUrl.isNotEmpty()){
        Picasso.get().load(it.userImageUrl).resize(200,200).centerCrop().into(binding.profileImageView)
        binding.fullNameTextView.text = it?.name?: ""
        binding.userProfileCaption.text = it?.userCaption?: ""
        binding.userNameText.text = it?.username?: ""



    }
}

        //Working with the recycler view
        val recyclerView : RecyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter =  UserPostAdapter(imageUrls, this)



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


        // navigate to editProfile Screen
        binding.editProfileButton.setOnClickListener(){
            if(userProfile?.email.toString().isNotEmpty()){

                viewModel.getCurrentProfileDetails(auth.currentUser?.email.toString())

                val bundle = Bundle()
                    val profile : Profile = viewModel.profile.value!!
                    bundle.putParcelable("profile", profile)
                    findNavController().navigate(R.id.action_userProfileFragment_to_editProfile, bundle)


            }
            else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
           

        }

        val bundle = Bundle()
        var buttonPressed : String
        bundle.putString("currentUserEmail", currentUser)
        binding.followingCountText.setOnClickListener(){
            buttonPressed = "following"
            bundle.putString("button", buttonPressed)
            findNavController().navigate(R.id.action_userProfileFragment_to_followingFragment, bundle)

        }
        binding.followersCountText.setOnClickListener(){
            buttonPressed = "followers"
            bundle.putString("button", buttonPressed)
            findNavController().navigate(R.id.action_userProfileFragment_to_followingFragment, bundle)
        }


    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable( "postInformation",imageUrls[position])
        findNavController().navigate(R.id.action_userProfileFragment_to_profilePostViewFragment, bundle)
    }
}
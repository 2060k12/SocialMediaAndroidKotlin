package com.phoenix.socialmedia.profile.follow

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.FollowingFragmentBinding
import com.phoenix.socialmedia.databinding.FollowingUsersRecyclerViewBinding
import com.phoenix.socialmedia.profile.ProfileViewModel
import com.phoenix.socialmedia.profile.adapter.FollowingListAdapter
import com.phoenix.socialmedia.utils.OnItemClickListener

class FollowingFragment : Fragment(), OnItemClickListener {

    lateinit var binding: FollowingFragmentBinding
    lateinit var adapterBinding : FollowingUsersRecyclerViewBinding

    private lateinit var recyclerView : RecyclerView
    private val viewModel: ProfileViewModel by viewModels()

    companion object {
        fun newInstance() = FollowingFragment()
    }

    private val profileViewModel = ProfileViewModel()


    private val followingListLiveData get() = profileViewModel.followings
    private val followersListLiveData get() = profileViewModel.followers
    private val followingList = ArrayList<Profile>()
    private val followersList = ArrayList<Profile>()
    private val currentUser get() = arguments?.getString("currentUserEmail")
    private val buttonPressed  get() = arguments?.getString("button")

    val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowingFragmentBinding.inflate(layoutInflater)
        adapterBinding = FollowingUsersRecyclerViewBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(buttonPressed.toString().lowercase() == "following"){

            recyclerView =binding.followingRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            if(auth.currentUser?.email.toString() == currentUser )
            recyclerView.adapter = FollowingListAdapter(followingList, "following", true, this)
            else recyclerView.adapter = FollowingListAdapter(followingList, "following", false, this )


            followingListLiveData.observe(viewLifecycleOwner){
                followingList.clear()
                followingList.addAll(it)
                recyclerView.adapter?.notifyDataSetChanged()
            }
            profileViewModel.getFollowing(currentUser!!)
        }

        else if(buttonPressed.toString().lowercase() == "followers") {
            recyclerView =binding.followingRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            if(auth.currentUser?.email.toString() == currentUser )
            recyclerView.adapter = FollowingListAdapter(followersList, "followers", true, this)
            else recyclerView.adapter = FollowingListAdapter(followersList, "followers", false, this)


            followersListLiveData.observe(viewLifecycleOwner){
                followersList.clear()
                followersList.addAll(it)
                recyclerView.adapter?.notifyDataSetChanged()
            }
            profileViewModel.getFollowers(currentUser!!)

        }






        }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()

        if(followingList.isNotEmpty()){
        viewModel.getUserProfileDetails(followingList[position].email){
            bundle.putParcelable("profile_info", it)
            findNavController().navigate(R.id.action_followingFragment_to_searchedProfileFragment, bundle)

        }}

        else
        {
            viewModel.getUserProfileDetails(followersList[position].email){
                bundle.putParcelable("profile_info", it)
                findNavController().navigate(R.id.action_followingFragment_to_searchedProfileFragment, bundle)

            }

        }

    }
}


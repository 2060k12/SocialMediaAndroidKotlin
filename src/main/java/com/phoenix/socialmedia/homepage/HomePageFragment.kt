package com.phoenix.socialmedia.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.HomePageFragmentBinding
import com.phoenix.socialmedia.homepage.posts.adapter.PostAdapter
import com.phoenix.socialmedia.homepage.posts.adapter.StoryAdapter

class HomePageFragment : Fragment() {

    private lateinit var binding: HomePageFragmentBinding
    private lateinit var auth: FirebaseAuth

    // Recycler view for post and story
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var storyRecyclerView: RecyclerView

    // Arraylist
    private  var newArrayList = ArrayList<Post>()
    private var storyArrayList = ArrayList<Story>()

    private lateinit var navController: NavController
    lateinit var postAdapter: PostAdapter


    companion object {
        fun newInstance() = HomePageFragment()
    }

    private val viewModel: HomePageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomePageFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        // For posts
        viewModel.getALlPost()
        postRecyclerView = binding.postRecyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.adapter = PostAdapter(newArrayList, navController)


        binding.progressBarHomePage.visibility = View.VISIBLE

        viewModel.post.observe(viewLifecycleOwner){
            post ->
            newArrayList.clear()
            newArrayList.addAll(post)
            binding.progressBarHomePage.visibility = View.GONE
            postRecyclerView.adapter?.notifyDataSetChanged()
        }
        viewModel.getALlPost()

        // For story
        storyRecyclerView = binding.storyRecyclerView
        storyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storyRecyclerView.setHasFixedSize(true)
        storyRecyclerView.adapter = StoryAdapter(storyArrayList)


        viewModel.story.observe(viewLifecycleOwner){
            story ->
            storyArrayList.clear()
            storyArrayList.addAll(story)
            storyRecyclerView.adapter?.notifyDataSetChanged()
        }

        viewModel.getAllStory()






    }



}

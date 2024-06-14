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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.Timestamp
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.HomePageFragmentBinding
import com.phoenix.socialmedia.homepage.posts.adapter.PostAdapter
import com.phoenix.socialmedia.homepage.posts.adapter.StoryAdapter
import com.phoenix.socialmedia.utils.OnItemClickListener

class HomePageFragment : Fragment(), OnItemClickListener{


    private var refreshCount=0
    private var time = Timestamp.now()
    private lateinit var binding: HomePageFragmentBinding

    // Recycler view for post and story
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var storyRecyclerView: RecyclerView

    // Arraylist
    private  var newArrayList = ArrayList<Post>()
    private var storyArrayList = ArrayList<Story>()

    private lateinit var navController: NavController
    lateinit var postAdapter: PostAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    companion object {
        fun newInstance() = HomePageFragment()
    }

    private val viewModel: HomePageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        val mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("Home", R.drawable.add, showBarState = true)

        // binding for our view
        binding = HomePageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // initializing a navController, which will be passed as an arguments in post Adapter
        // since it is not possible to call navController from an adapter
        navController = findNavController()

        // For posts
        viewModel.getALlPost()
        postRecyclerView = binding.postRecyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.setItemViewCacheSize(5)
        postRecyclerView.adapter = PostAdapter(newArrayList, navController)
        binding.progressBarHomePage.visibility = View.VISIBLE

        viewModel.post.observe(viewLifecycleOwner){
            post ->
            newArrayList.clear()
            newArrayList.addAll(post )
            binding.progressBarHomePage.visibility = View.GONE
            postRecyclerView.adapter?.notifyDataSetChanged()
        }
        viewModel.getALlPost()

        // For story
        storyRecyclerView = binding.storyRecyclerView
        storyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storyRecyclerView.setHasFixedSize(true)
        storyRecyclerView.adapter = StoryAdapter(storyArrayList, this)


        viewModel.story.observe(viewLifecycleOwner){
            story ->
            storyArrayList.clear()
            storyArrayList.addAll(story)
            storyRecyclerView.adapter?.notifyDataSetChanged()
        }

        viewModel.getAllStory()


        swipeRefreshLayout = binding.swipeRefreshLayoutHomePage
        swipeRefreshLayout.setOnRefreshListener {
            if(refreshCount >= 4 ){
                refreshCount =0
            }
            if(refreshCount== 0) {
                viewModel.getALlPost()
                newArrayList.clear()
                newArrayList.addAll(newArrayList)
                postRecyclerView.adapter?.notifyDataSetChanged()

            }
            refreshCount ++


            swipeRefreshLayout.isRefreshing = false
        }


    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putParcelableArrayList("storyList", storyArrayList)
        findNavController().navigate(R.id.action_homePageFragment_to_storyViewFragment, bundle)
    }




}

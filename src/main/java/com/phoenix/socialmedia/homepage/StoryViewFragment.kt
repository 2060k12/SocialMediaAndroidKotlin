package com.phoenix.socialmedia.homepage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.databinding.StoryViewFragmentBinding
import com.phoenix.socialmedia.homepage.posts.adapter.StoryClickedAdapter

class StoryViewFragment : Fragment() {

    // declaring here but to be initialized later in the code
    private lateinit var mainActivity : MainActivity
    private lateinit var binding: StoryViewFragmentBinding
    private lateinit var pager :ViewPager2
    private val storyList @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    get() = arguments?.getParcelableArrayList("storyList", Story::class.java)!!
    private val position get() = arguments?.getInt("position")!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Setting up action bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("Story", R.drawable.add, showBarState = true, true)
        mainActivity.getNavigationBar().visibility = View.GONE
        // Inflate the layout for this fragment
        binding = StoryViewFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Pager2 is works like  recycler view but here its easier if we want to show something in a different page and getting current item is more easier as well
        pager = binding.storyViewRecyclerView
        pager.adapter = StoryClickedAdapter(storyList = storyList)
        pager.setCurrentItem(position, false)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }
}
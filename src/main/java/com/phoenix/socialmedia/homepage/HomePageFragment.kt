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
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.HomePageFragmentBinding
import com.phoenix.socialmedia.homepage.posts.PostAdapter

class HomePageFragment : Fragment() {

    private lateinit var binding: HomePageFragmentBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var newRecyclerView: RecyclerView
    private  var newArrayList = ArrayList<Post>()


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

        binding.addNewPostFloatingButton.setOnClickListener(){
            val navController: NavController = findNavController()
            navController.navigate(R.id.action_homePageFragment_to_cameraFragment)
        }

        binding.signOut.setOnClickListener(){
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)

        }


//        newArrayList = arrayListOf(post,post4,post24,post35,post3)

         viewModel.getALlPost()
        newRecyclerView = binding.recyclerView
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.adapter = PostAdapter(newArrayList)


        binding.progressBarHomePage.visibility = View.VISIBLE

        viewModel.post.observe(viewLifecycleOwner){
            post ->
            newArrayList.clear()
            newArrayList.addAll(post)
            binding.progressBarHomePage.visibility = View.GONE

            newRecyclerView.adapter?.notifyDataSetChanged()
        }


        viewModel.getALlPost()









    }


}

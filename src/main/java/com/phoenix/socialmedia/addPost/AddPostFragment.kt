package com.phoenix.socialmedia.addPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.AddPostFragmentBinding

class AddPostFragment : Fragment() {
    lateinit var binding: AddPostFragmentBinding

    companion object {
        fun newInstance() = AddPostFragment()
    }

    private val viewModel: AddPostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddPostFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This button takes the user back to the home page
        binding.backButton.setOnClickListener(){
            var navController : NavController = findNavController()
            navController.navigate(R.id.action_addPostFragment_to_homePageFragment)
        }


        // Button to upload new post
        binding.addNewPostButton.setOnClickListener(){
            var postCaption :String  =binding.postTextBox.text.toString()
            var imageUrl: String = binding.postImageUrlTextBox.text.toString()
            var timeStamp :java.util.Date =Timestamp.now().toDate()
            viewModel.uploadPost(postText = postCaption, postImageUrl = imageUrl, timestamp = timeStamp){uploadSuccess ->
                if(uploadSuccess){
                    var navController : NavController = findNavController()
                    navController.navigate(R.id.action_addPostFragment_to_homePageFragment)
                    Toast.makeText(context, "New Post Uploaded", Toast.LENGTH_SHORT).show()
                }

                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }

        }



    }
}
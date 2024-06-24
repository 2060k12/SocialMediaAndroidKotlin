package com.phoenix.socialmedia.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.LoadClickedImageFragmentBinding
import com.squareup.picasso.Picasso


class LoadClickedImageFragment : Fragment() {

    private lateinit var binding : LoadClickedImageFragmentBinding
    private val viewModel = CameraViewModel()

    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("Upload", R.drawable.add, showBarState = true, true)
        mainActivity.getNavigationBar().visibility = View.GONE

        // Inflate the layout for this fragment
        binding = LoadClickedImageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val imageUrl = arguments?.getParcelable<Uri>("imageUri")
        val imageUrl = arguments?.getString("imageUri")?.toUri()
        if(imageUrl != null){
            Picasso.get().load(imageUrl).into(binding.clickedImageView)
        }

        binding.uploadClickedImageButton.setOnClickListener {
            binding.uploadImageProgressBar.visibility = View.VISIBLE
            val caption = binding.addCaptionTextBox.text.toString()
            viewModel.uploadImage(imageUrl!!){
                downloadUrl ->
                if(downloadUrl != null){
                    val timeStamp :java.util.Date = Timestamp.now().toDate()
                    viewModel.uploadPost(caption,downloadUrl.toString(), timeStamp){uploadSuccess ->
                        if(uploadSuccess){
                            val navController : NavController = findNavController()
                            navController.navigate(R.id.action_loadClickedImageFragment_to_homePageFragment)
                            binding.uploadImageProgressBar.visibility = View.GONE

                            Toast.makeText(context, "New Post Uploaded", Toast.LENGTH_SHORT).show()
                        }

                        else{
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        }

        binding.addToStoryButton.setOnClickListener {
            val timeStamp :java.util.Date = Timestamp.now().toDate()
            binding.uploadImageProgressBar.visibility = View.VISIBLE

            viewModel.uploadImage(imageUrl!!){
                downloadUrl ->
                    if (downloadUrl != null){
                        val timeStamp :java.util.Date = Timestamp.now().toDate()
                        viewModel.uploadStory(downloadUrl, timeStamp)
                        val navController : NavController = findNavController()
                        navController.navigate(R.id.action_loadClickedImageFragment_to_homePageFragment)
                        binding.uploadImageProgressBar.visibility = View.GONE
                    }

            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }

}
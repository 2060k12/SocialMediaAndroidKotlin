package com.phoenix.socialmedia.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phoenix.socialmedia.databinding.LoadClickedImageFragmentBinding
import com.squareup.picasso.Picasso


class LoadClickedImageFragment : Fragment() {

    private lateinit var binding : LoadClickedImageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LoadClickedImageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString("imageUrl")
        if(imageUrl != null){
            Picasso.get().load(imageUrl).into(binding.clickedImageView)
        }




    }

}
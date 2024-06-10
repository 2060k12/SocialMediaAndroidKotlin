package com.phoenix.socialmedia.profile.editUserProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.EditProfileInformationFragmentBinding

class EditProfileInformationFragment : Fragment() {
  // adding binding
    lateinit var binding: EditProfileInformationFragmentBinding
  // Initialization of  viewmodel
    private val viewModel = EditProfileViewModel()

    private val editType  get() = arguments?.getString("editType")
    private val editInformation get() = arguments?.getString("editInformation")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = EditProfileInformationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editProfileInformationTextLayout.hint = editType?.uppercase()
        binding.editProfileInformationTextBox.setText(editInformation)
        binding.editProfileInformationTextBox.requestFocus()


        binding.button.setOnClickListener(){
            binding.editInformationProgressBar.visibility = View.VISIBLE
            if(binding.editProfileInformationTextBox.text.toString().isNotEmpty()&& editType != null){
                
                viewModel.updateUserInformation(editType.toString(), binding.editProfileInformationTextBox.text.toString())
                binding.editInformationProgressBar.visibility = View.GONE
                findNavController().navigate(R.id.action_editProfileInformationFragment_to_userProfileFragment )

            }
            else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                binding.editInformationProgressBar.visibility = View.GONE

            }
        }



    }
}
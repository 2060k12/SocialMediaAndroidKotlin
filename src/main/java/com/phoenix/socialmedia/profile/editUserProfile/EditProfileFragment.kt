package com.phoenix.socialmedia.profile.editUserProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.EditProfileFragmentBinding
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment() {

    lateinit var binding: EditProfileFragmentBinding

    val userProfile : Profile?
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() {
        return arguments?.getParcelable("profile", Profile::class.java)
    }
    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val viewModel: EditProfileViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setting up action bar
        val mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("Edit Profile", R.drawable.add, showBarState = true, true)

        binding = EditProfileFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    @SuppressLint("SetTextI18n", "IntentReset")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Loading all information
        binding.fullNameProfileTextView.text = userProfile?.name.toString()
        binding.bioProfileTextView.text = userProfile?.userCaption.toString()
        binding.userNameProfileTextView.text = userProfile?.username.toString()
        if(userProfile?.userImageUrl.toString().isNotEmpty()) {
            Picasso.get().load(userProfile?.userImageUrl).resize(200, 200).centerCrop()
                .into(binding.imageView)
        }

        // When FullName card view is clicked
        binding.editNameCardView.setOnClickListener {
            editInformation(editType = "name", editInformation = binding.fullNameProfileTextView.text.toString() )
        }


        // When Username card view is clicked
        binding.editUserNameCardView.setOnClickListener {
            editInformation(editType = "username", editInformation = binding.userNameProfileTextView.text.toString() )
        }
        // When Bio card view is clicked
        binding.editBioCardView.setOnClickListener {
            editInformation(editType = "userCaption", editInformation = binding.bioProfileTextView.text.toString() )
        }

        // Logout button
        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_editProfile_to_loginFragment)

        }

        // editProfileImage
        binding.imageView.setOnClickListener(){
            val getImageFromGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageFromGalleryIntent.type = "image/*"
            startActivityForResult(getImageFromGalleryIntent, 100)


        }

        binding.deleteAccountButton.setOnClickListener{
            val button = Button(requireContext())
            button.text = "Confirm Delete"
            button.setBackgroundColor(Color.RED)
            button.setTextColor(Color.WHITE)

            // alert box
            val alert =AlertDialog.Builder(context)
                .setView(
                   button
                ).show()

            button.setOnClickListener{
                viewModel.deleteAccount()
                findNavController().navigate(R.id.action_editProfile_to_loginFragment)
            }
        }


    }
    private fun editInformation(editType: String, editInformation: String){
        val bundle = bundleOf(
            "editType" to editType,
            "editInformation" to editInformation
        )

        findNavController().navigate(R.id.action_editProfile_to_editProfileInformationFragment, bundle)

    }




    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            val selectedImageUri = data?.data
            if(selectedImageUri!= null){
                viewModel.updateProfileImage(selectedImageUri)
                Toast.makeText(context, "New Profile Picture Added", Toast.LENGTH_SHORT).show()

            }
        }

    }

}
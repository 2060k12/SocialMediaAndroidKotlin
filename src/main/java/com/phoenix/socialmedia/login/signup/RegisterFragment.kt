package com.phoenix.socialmedia.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    lateinit var binding: RegisterFragmentBinding
    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: RegisterViewModel by viewModels()

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Navigation and action Bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("", R.drawable.add, showBarState = false, false)
        mainActivity.getNavigationBar().visibility = View.GONE

        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.CreateNewAccountButton.setOnClickListener(){
            var email = binding.emailTextBox.text.toString()
            var password = binding.passwordTextBox.text.toString()
            var userName = binding.userNameTextBox.text.toString()
            var name = binding.nameTextBox.text.toString()
            binding.registerProgressBar.visibility = View.VISIBLE


            // TODO: Change the hardcoded value, make a new ui for Register page
            viewModel.signUp(
                email = email,
                password = password,
                userName = userName,
                name = name
            ) { success ->
                if (success) {
                    binding.registerProgressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_homePageFragment)
                } else {
                    binding.registerProgressBar.visibility = View.GONE
                    Toast.makeText(context, "Error!, Signing In", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }
}
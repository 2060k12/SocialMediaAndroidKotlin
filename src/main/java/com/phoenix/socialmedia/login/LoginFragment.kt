package com.phoenix.socialmedia.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    companion object {
        fun newInstance() = LoginFragment()

    }

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        /*
            * When the view is created, the app will check if the user is logged In already.
            * If yes, This app will automatically take the user to home screen
            * Else, A login screen is shown to the user
        */
        var status = viewModel.checkCurrentUser()

        if (status) {
            var navController: NavController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_homePageFragment)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var email: String
        var password: String

        binding.loginButton.setOnClickListener {
            email = binding.emailTextBox.text.toString()
            password = binding.passwordTextBox.text.toString()
            binding.progressBar.visibility = View.VISIBLE
            // This function will login the user if the
            viewModel.signIn(email = email, password = password) { success ->
                if (success) {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)


                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error!, Logging In", Toast.LENGTH_SHORT).show()
                }
            }


        }


        binding.createNewAccountButton.setOnClickListener {
            var email = binding.emailTextBox.text.toString()
            var password = binding.passwordTextBox.text.toString()
            binding.progressBar.visibility = View.VISIBLE


            // TODO: Change the hardcoded value, make a new ui for Register page
            viewModel.signUp(
                email = email,
                password = password,
                userName = "pranish",
                name = "Pranish Pathak"
            ) { success ->
                if (success) {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error!, Signing In", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}
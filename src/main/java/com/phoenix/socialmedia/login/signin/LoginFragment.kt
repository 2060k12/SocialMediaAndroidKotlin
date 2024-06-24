package com.phoenix.socialmedia.login.signin

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    companion object {
        fun newInstance() = LoginFragment()

    }

    private val viewModel: LoginViewModel by viewModels()
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Navigation and action Bar
        mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("", R.drawable.add, showBarState = false, false)
        mainActivity.getNavigationBar().visibility = View.GONE

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        /*
            * When the view is created, the app will check if the user is logged In already.
            * If yes, This app will automatically take the user to home screen
            * Else, A login screen is shown to the user
        */
        var status = viewModel.checkCurrentUser()

        if (status) {

            val navController: NavController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_homePageFragment)

        }

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var email: String
        var password: String

        binding.loginButton.setOnClickListener {
            email = binding.emailTextInput.text.toString()
            password = binding.passwordTextInput.text.toString()
            // This function will login the user
            if(email.isNotEmpty() && password.isNotEmpty()){
                binding.progressBar.visibility = View.VISIBLE
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
            else{
                Toast.makeText(context, "Enter Both Email and Password First", Toast.LENGTH_SHORT).show()
            }
        }


        binding.createNewAccountButton.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE

    }

}
package com.phoenix.socialmedia.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.MessagesOverviewFragmentBinding
import com.phoenix.socialmedia.utils.OnItemClickListener

class MessagesOverviewFragment : Fragment(), OnItemClickListener {

    private val listOfMessages = ArrayList<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: MessagesOverviewFragmentBinding

    private val viewModel: MessageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.actionBar("Messages", R.drawable.add, showBarState = true, true)

        // Inflate the layout for this fragment
        binding = MessagesOverviewFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.messageListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        val adapter = MessagesOverviewAdapter(listOfMessages, this)
        recyclerView.adapter = adapter

//        We will get the the name of users to whom we have messaged from current account
//        To get the email of current user we will be using firebase auth, directly in repository, so we don't need to pass any arguments in this function
        viewModel.getAllMessageOverview()

        viewModel.conversationList.observe(viewLifecycleOwner){
            list ->
            val newList = list.subList(listOfMessages.size, list.size)
            listOfMessages.addAll(newList)
            viewModel.getAllMessageOverview()
            adapter.notifyDataSetChanged()
        }



    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("messageOf", listOfMessages[position] )
        findNavController().navigate(R.id.action_messagesOverviewFragment_to_messageFragment, bundle)
        viewModel.setReadStatus(messageOf = listOfMessages[position])
    }
}
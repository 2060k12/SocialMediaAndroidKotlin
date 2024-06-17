package com.phoenix.socialmedia.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.data.Messages
import com.phoenix.socialmedia.databinding.MessageFragmentBinding

class MessageFragment : Fragment() {

    private lateinit var binding : MessageFragmentBinding
    private lateinit var recyclerView : RecyclerView
    private var messageList = ArrayList<Messages>()

    companion object {
        fun newInstance() = MessageFragment()
    }

    private val viewModel: MessageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MessageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMessage("pranishpathak100@gmail.com")

        // initializing recycler view
        recyclerView = binding.messageRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = MessageAdapter(messageList)

        viewModel.messageList.observe(viewLifecycleOwner){
            message ->
            messageList.clear()
            messageList = message
            recyclerView.adapter = MessageAdapter(messageList)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        viewModel.getMessage("pranishpathak100@gmail.com")


        binding.sendMessageButton.setOnClickListener{
            viewModel.uploadMessage("pranishpathak100@gmail.com", "Hello Mate, Sup?")
        }


    }
}
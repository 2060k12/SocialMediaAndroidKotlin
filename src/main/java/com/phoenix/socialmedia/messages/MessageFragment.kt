package com.phoenix.socialmedia.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.MainActivity
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Messages
import com.phoenix.socialmedia.databinding.MessageFragmentBinding
import com.phoenix.socialmedia.homepage.HomePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageFragment : Fragment() {

    private lateinit var binding: MessageFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private var messageList = ArrayList<Messages>()
    lateinit var userImageLink: String
    private lateinit var userName: String
    private val sendMessageTo get() = arguments?.getString("messageOf")

    companion object {
        fun newInstance() = MessageFragment()
    }

    private val viewModel: MessageViewModel by viewModels()
    private val homePageViewModel: HomePageViewModel by viewModels()
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // getting userName and image
        mainActivity = requireActivity() as MainActivity

        homePageViewModel.getUserProfileImage(sendMessageTo!!) { image, name ->
            userImageLink = image
            userName = name
            mainActivity.actionBar(userName, R.drawable.add, showBarState = true, true)

        }
        binding = MessageFragmentBinding.inflate(inflater, container, false)
        mainActivity.getNavigationBar().visibility = View.GONE
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReadStatus(sendMessageTo!!) { status ->
            if (status.lowercase() == "seen") {
                binding.chip.text = "Seen"
            } else {
                binding.chip.text = "Sent"
            }
        }

        // initializing recycler view
        recyclerView = binding.messageRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        // adapter
        val adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter

        viewModel.messageList.observe(viewLifecycleOwner) { message ->
            viewModel.getMessage(sendMessageTo!!)
            if (message.size > messageList.size) {
                setStatus()
                val newMessages = message.subList(messageList.size, message.size)
                messageList.addAll(newMessages)
                recyclerView.adapter?.notifyItemRangeInserted(messageList.size, newMessages.size)
                recyclerView.scrollToPosition(messageList.lastIndex)


            }
            viewModel.getReadStatus(sendMessageTo!!) { status ->
                if (status.lowercase() == "seen") {
                    binding.chip.text = "Seen"
                } else if (status.lowercase() == "received") {
                    binding.chip.text = "Sent"
                    viewModel.setReadStatus(sendMessageTo!!)
                } else {
                    binding.chip.text = "Sent"
                }
            }
        }
        viewModel.getMessage(sendMessageTo!!)

        binding.sendMessageButton.setOnClickListener {
            viewModel.uploadMessage(sendMessageTo!!, binding.messageEditText.text.toString())
            viewModel.getMessage(sendMessageTo!!)
            binding.messageEditText.setText("")
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.getNavigationBar().visibility = View.VISIBLE
    }

    private fun setStatus(){
        CoroutineScope(Dispatchers.Main).launch{
            delay(1000)
            viewModel.setReadStatus(sendMessageTo!!)

        }

    }

}

package com.phoenix.socialmedia.homepage.posts.comment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.data.Comments
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.databinding.CommentFragmentBinding
import com.phoenix.socialmedia.homepage.posts.adapter.CommentAdapter

class CommentFragment : DialogFragment() {
    private lateinit var binding: CommentFragmentBinding

    lateinit var recyclerView : RecyclerView
    private var commentArrayList =ArrayList<Comments>()
    companion object {
        fun newInstance() = CommentFragment()
    }

    private val viewModel: CommentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting data from the previous screen which was stored in Post class
        // Using bundle
        val post = arguments?.getParcelable("postInfo", Post::class.java)
        val postId = post?.postId
        val email = post?.email


        // When user press the "uploadComment" button
        binding.uploadComment.setOnClickListener {
            val comment = binding.commentTextBox.text
            viewModel.commentThePost(postId= postId.toString(), email =email.toString(), comment = comment.toString())
            viewModel.getAllComments(email!!,postId!!)
            Toast.makeText(context, "Comment Uploaded", Toast.LENGTH_SHORT).show()
            binding.commentTextBox.setText("")

            
        }

        val adapter = CommentAdapter(commentArrayList)
        recyclerView = binding.commentViewRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


    viewModel.comments.observe(viewLifecycleOwner){
        commentArrayList.clear()
        commentArrayList.addAll(it)
        recyclerView.adapter?.notifyDataSetChanged()
    }
        viewModel.getAllComments(email!!,postId!!)
    }



    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.BOTTOM)

    }
}
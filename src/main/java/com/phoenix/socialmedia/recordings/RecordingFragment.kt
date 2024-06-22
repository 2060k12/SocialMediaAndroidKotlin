package com.phoenix.socialmedia.recordings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.phoenix.socialmedia.databinding.RecordingFragmentBinding

class RecordingFragment : Fragment() {

    lateinit var binding : RecordingFragmentBinding
    companion object {
        fun newInstance() = RecordingFragment()
    }
    private val viewModel: RecordingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RecordingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}
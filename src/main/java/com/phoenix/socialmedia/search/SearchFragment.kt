package com.phoenix.socialmedia.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.SearchFragmentBinding
import com.phoenix.socialmedia.search.adapter.SearchedResultAdapter
import com.phoenix.socialmedia.utils.OnItemClickListener

class SearchFragment : Fragment(), OnItemClickListener {
    //binding for our view (Search Fragment
    private lateinit var binding : SearchFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private var searchedUsers = ArrayList<Profile>()

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.searchedUsersRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adView = binding.adView2
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        var searchText: String =""
        recyclerView.adapter = SearchedResultAdapter(searchedUsers, this)

        binding.searchBarInputText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = binding.searchBarInputText.text.toString()
                viewModel.getSearchResult(searchText = searchText)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        viewModel.searchResults.observe(viewLifecycleOwner){
            results ->
            searchedUsers.clear()
            searchedUsers.addAll(results)
            recyclerView.adapter?.notifyDataSetChanged()

        }





    }

    override fun onItemClick(position: Int) {
        val bundle =  Bundle()
        val user = searchedUsers[position]
        bundle.putParcelable("profile_info", user)
        findNavController().navigate(R.id.action_searchFragment_to_searchedProfileFragment, bundle)
    }
}
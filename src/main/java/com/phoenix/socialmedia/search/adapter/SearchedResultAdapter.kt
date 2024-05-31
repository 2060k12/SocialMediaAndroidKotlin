package com.phoenix.socialmedia.search.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.databinding.SearchResultRecyclerViewBinding
import com.squareup.picasso.Picasso

class SearchedResultAdapter(private var searchedUsers : ArrayList<Profile>, private val itemClickListener: OnItemClickListener) :RecyclerView.Adapter<SearchedResultAdapter.searchResultViewHolder>(){

    lateinit var binding: SearchResultRecyclerViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchResultViewHolder {
    binding = SearchResultRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return searchResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: searchResultViewHolder, position: Int) {
        val currentItem = searchedUsers[position]
        Picasso.get().load(currentItem.userImageUrl?: "https://cdn.pixabay.com/photo/2020/10/11/19/51/cat-5646889_1280.jpg").resize(200,200).centerCrop().into(holder.searchedUserImageView)
        holder.searchUserName.text = currentItem.username




    }

    override fun getItemCount(): Int {
        return searchedUsers.size
    }

    inner class searchResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val searchedUserImageView : ImageView = itemView.findViewById(R.id.searchedUserProfileImageView)
        val searchUserName : TextView = itemView.findViewById(R.id.searchedUserNameTextView)

        init {
            itemView.setOnClickListener {

                v: View ->
                    val position: Int = layoutPosition
                    itemClickListener.onItemClick(position)


            }
        }



    }


}

interface OnItemClickListener{
    fun onItemClick(position: Int)

}
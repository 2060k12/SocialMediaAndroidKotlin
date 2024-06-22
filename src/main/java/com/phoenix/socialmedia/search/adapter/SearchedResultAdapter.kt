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
import com.phoenix.socialmedia.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class SearchedResultAdapter(private var searchedUsers : ArrayList<Profile>, private val itemClickListener: OnItemClickListener) :RecyclerView.Adapter<SearchedResultAdapter.SearchResultViewHolder>(){

    lateinit var binding: SearchResultRecyclerViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
    binding = SearchResultRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SearchResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val currentItem = searchedUsers[position]
        if(currentItem.userImageUrl.isNotEmpty()){

            Picasso.get().load(currentItem.userImageUrl).resize(200,200).centerCrop().into(holder.searchedUserImageView)
        }
        holder.searchUserName.text = currentItem.username




    }

    override fun getItemCount(): Int {
        return searchedUsers.size
    }

    inner class SearchResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
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

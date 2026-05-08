package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class PostsAdapter(private var posts: List<String>): RecyclerView.Adapter<PostsAdapter.PostVHolder>() {
    // update posts
    fun update_Data(newPosts: List<String>){
        posts = newPosts
        notifyDataSetChanged()

    }

    // view holder for post
    class PostVHolder(view: View) : RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.postImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVHolder {
        // inflate post layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        // set image size
        val size = parent.context.resources.displayMetrics.widthPixels / 3
        view.layoutParams = RecyclerView.LayoutParams(size, size)
        return PostVHolder(view)
    }

    override fun onBindViewHolder(holder: PostVHolder, position: Int ) {
        // load post image
        Glide.with(holder.itemView.context)
            .load(posts[position])
            .centerCrop()
            .into(holder.image)

    }

    override fun getItemCount() = posts.size
    }

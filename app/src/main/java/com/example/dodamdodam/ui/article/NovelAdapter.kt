package com.example.dodamdodam.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Novel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class NovelAdapter(private val novels: List<Novel>, private val onItemClicked: (Novel) -> Unit) : RecyclerView.Adapter<NovelAdapter.NovelViewHolder>() {

    class NovelViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_novel, parent, false)) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView_novel_image)
        private var titleView: TextView = itemView.findViewById(R.id.textView_novel_name)
        private var authorView: TextView = itemView.findViewById(R.id.textView_novel_quthor)

        fun bind(novel: Novel, context: Context) { // Pass context as a parameter.
            val storageRef = FirebaseStorage.getInstance().reference
            val photoRef: StorageReference = storageRef.child("novel/${novel.image}.png")

            // 다운로드 URL 가져오기
            photoRef.downloadUrl
                .addOnSuccessListener { uri ->
                    print("asdfasdf")
                    // 다운로드 URL을 사용하여 사진을 표시하거나 처리
                    val downloadUrl = uri.toString()
                    Glide.with(context) // Use the passed context here.
                        .load(downloadUrl)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(imageView)

                }
            titleView.text = novel.title
            authorView.text = novel.author
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NovelViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NovelViewHolder, position: Int) {
        val novel = novels[position]
        holder.itemView.setOnClickListener { onItemClicked(novel) }
        holder.bind(novel, holder.itemView.context) // Pass the context from the itemView.
    }


    override fun getItemCount(): Int = novels.size
}
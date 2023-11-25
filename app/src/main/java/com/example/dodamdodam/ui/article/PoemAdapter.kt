package com.example.dodamdodam.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Poem

class PoemAdapter(private val poems: List<Poem>) : RecyclerView.Adapter<PoemAdapter.PoemViewHolder>() {

    class PoemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_poem, parent, false)) {
        private var textViewTitle: TextView? = null
        private var textViewAuthor: TextView? = null

        init {
            textViewTitle = itemView.findViewById(R.id.textView_poem_title)
            textViewAuthor = itemView.findViewById(R.id.textView_poem_author)
        }

        fun bind(poem: Poem) {
            textViewTitle?.text = poem.title
            textViewAuthor?.text = poem.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PoemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PoemViewHolder, position: Int) {
        val poem: Poem = poems[position]
        holder.bind(poem)
    }

    override fun getItemCount(): Int = poems.size
}

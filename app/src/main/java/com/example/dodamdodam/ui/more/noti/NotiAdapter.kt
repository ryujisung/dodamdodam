package com.example.dodamdodam.ui.more.noti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Noti

class NotiAdapter(private val notiList: List<Noti>) : RecyclerView.Adapter<NotiAdapter.NotiViewHolder>() {

    class NotiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNotice: TextView = itemView.findViewById(R.id.textView_question)

        fun bind(noti: Noti) {
            textViewNotice.text = noti.notice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_noti, parent, false)
        return NotiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.bind(notiList[position])
    }

    override fun getItemCount() = notiList.size
}

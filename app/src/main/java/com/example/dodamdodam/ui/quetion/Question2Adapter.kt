package com.example.dodamdodam.ui.quetion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Question2

class Question2Adapter(private var questions: List<Question2> = listOf()) : RecyclerView.Adapter<Question2Adapter.Question2ViewHolder>() {

    fun setQuestions(questions: List<Question2>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Question2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
        return Question2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Question2ViewHolder, position: Int) {
        val question = questions[position]
        holder.nameTextView.text = question.name
        holder.answerTextView.text = question.answer
    }

    override fun getItemCount() = questions.size

    class Question2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textView_name)
        val answerTextView: TextView = itemView.findViewById(R.id.textView_answer)
    }
}
package com.example.dodamdodam.ui.quetion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Question

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var questions: List<Question> = listOf() // 질문 객체 리스트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quetion, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question.question) // Question 객체의 question 속성을 사용
    }

    override fun getItemCount() = questions.size

    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewQuestion: TextView = itemView.findViewById(R.id.textView_question)

        fun bind(questionText: String) {
            textViewQuestion.text = questionText
        }
    }
}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Question

class QuestionAdapter(private val onQuestionClick: (Question) -> Unit) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var questions: List<Question> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quetion, parent, false)
        return QuestionViewHolder(view, onQuestionClick)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, (position + 1).toString() + ". " + question.question)
    }

    override fun getItemCount() = questions.size

    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(itemView: View, private val onQuestionClick: (Question) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val textViewQuestion: TextView = itemView.findViewById(R.id.textView_question)

        fun bind(question: Question, questionText: String) {
            textViewQuestion.text = questionText
            itemView.setOnClickListener {
                onQuestionClick(question)
            }
        }
    }
}

package com.example.dodamdodam.ui.quetion

import QuestionAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Poem
import com.example.dodamdodam.model.Question
import com.example.dodamdodam.model.UserInfo
import com.example.dodamdodam.ui.article.PoemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class QuetionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quetion, container, false)
        recyclerView = view.findViewById(R.id.recyclerView_questions)

        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter { question ->
            val intent = Intent(activity, QuestionActivity::class.java).apply {
                putExtra("question", question as Serializable)
            }
            startActivity(intent)
        }
        recyclerView.adapter = questionAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        firestore = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        firestore.collection(user?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val date = userInfo[0].createdate
                firestore.collection("question")
                    .get()
                    .addOnSuccessListener { documents ->
                        val question = documents.toObjects(Question::class.java)
                        var day = daysUntilNow(date)
                        if(day > question.size) {
                            day = question.size
                        }
                        questionAdapter.setQuestions(question.subList(0,day ).reversed())

                    }
                    .addOnFailureListener { exception ->
                        Log.e("ddd", exception.toString())
                    }

            }
            .addOnFailureListener { exception ->
                Log.e("ddd", exception.toString())
            }

    }
    private fun daysUntilNow(dateString: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val pastDate = format.parse(dateString)
        val currentDate = Calendar.getInstance().time

        return ((currentDate.time - pastDate.time) / (1000 * 60 * 60 * 24)).toInt()
    }

}

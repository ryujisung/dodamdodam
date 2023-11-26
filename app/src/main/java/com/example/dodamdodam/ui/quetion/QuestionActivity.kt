package com.example.dodamdodam.ui.quetion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityQuestionBinding
import com.example.dodamdodam.model.Family
import com.example.dodamdodam.model.Question
import com.example.dodamdodam.model.Question2
import com.example.dodamdodam.model.UserInfo
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuestionActivity : BaseActivity<ActivityQuestionBinding>(R.layout.activity_question) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Question2Adapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val question = intent.getSerializableExtra("question") as? Question
        // 또는 Parcelable을 사용하는 경우: val question = intent.getParcelableExtra<Question>("question")

        val textView = findViewById<TextView>(R.id.txt_question)
        val nametxt = findViewById<TextView>(R.id.textView_name)
        val answer = findViewById<TextView>(R.id.textView_answer)
        textView.text = question?.question
        adapter = Question2Adapter()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_questions)
        adapter = Question2Adapter() // 어댑터를 한 번만 생성합니다.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter // 생성된 어댑터를 설정합니다.

        firestore = FirebaseFirestore.getInstance()
        // 예제 데이터
        val user = FirebaseAuth.getInstance().currentUser
        firestore.collection(user?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val familycode = userInfo[0].family
                val name = userInfo[0].name
                firestore.collection("family")
                    .document("family")
                    .collection(familycode)
                    .document(familycode)
                    .collection("answer")
                    .get()
                    .addOnSuccessListener { documents ->
                        var questionsList = documents.toObjects(Question2::class.java)
                        questionsList = questionsList.filter { question2 ->
                            question2.question == question?.question
                        }
                        // Family 문서를 가져온 후 성공적으로 가져오면 실행되는 콜백
                        firestore.collection("family")
                            .document("family")
                            .collection(familycode)
                            .document(familycode)
                            .get()
                            .addOnSuccessListener { document ->
                                val family = document.toObject(Family::class.java)
                                var filteredQuestionsList = questionsList.filter { question2 ->
                                    family?.name != question2.name
                                }
                                var filteredQuestionsList2 = questionsList.filter { question2 ->
                                    family?.name == question2.name
                                }
                                filteredQuestionsList =filteredQuestionsList+filteredQuestionsList2
                                Log.e("dd", questionsList.toString())
                                Log.e("dd", family.toString())
                                Log.e("dd", filteredQuestionsList.toString())
                                var bindlist = questionsList.filter { question2 ->
                                    name == question2.name
                                }
                                Log.e("dd", bindlist[0].name.toString())
                                nametxt.text = bindlist[0].name
                                answer.text = bindlist[0].answer

                                filteredQuestionsList = questionsList.filter { question2 ->
                                    name != question2.name
                                }

                               adapter.setQuestions(filteredQuestionsList)
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ddd", exception.toString())
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("ddd", exception.toString())
            }


    }
}
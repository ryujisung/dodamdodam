package com.example.dodamdodam.ui.quetion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class QuestionActivity : BaseActivity<ActivityQuestionBinding>(R.layout.activity_question) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Question2Adapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question)
        val question = intent.getSerializableExtra("question") as? Question
        // 또는 Parcelable을 사용하는 경우: val question = intent.getParcelableExtra<Question>("question")

        val textView = findViewById<TextView>(R.id.txt_question)
        val nametxt = findViewById<TextView>(R.id.textView_name)
        val answer = findViewById<EditText>(R.id.textView_answer)
        textView.text = question?.question
        adapter = Question2Adapter()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_questions)
        adapter = Question2Adapter() // 어댑터를 한 번만 생성합니다.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter // 생성된 어댑터를 설정합니다.
        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        binding.questionBtnSubmit.setOnClickListener {
            val answerText = binding.textViewAnswer.text.toString()
            firestore.collection(user?.email.toString())
                .get()
                .addOnSuccessListener { documents ->
                    val userInfo = documents.toObjects(UserInfo::class.java)
                    val familycode = userInfo[0].family
                    val name = userInfo[0].name
                    // Firebase에 저장하기
                    saveAnswerToFirebase(
                        name,
                        question?.question.toString(),
                        answerText,
                        familycode
                    )
                }
        }
            // 예제 데이터

            firestore.collection(user?.email.toString())
                .get()
                .addOnSuccessListener { documents ->
                    val userInfo = documents.toObjects(UserInfo::class.java)
                    val familycode = userInfo[0].family
                    val name = userInfo[0].name
                    // ... 기존 코드 ...

                    firestore.collection("family")
                        .document("family")
                        .collection(familycode)
                        .get()
                        .addOnSuccessListener { document ->
                            val families = document.toObjects(Family::class.java)
                            val familyNames = families.map { it.name } // 가족 구성원의 이름 리스트

                            firestore.collection("family")
                                .document("family")
                                .collection(familycode)
                                .document(familycode)
                                .collection("answer")
                                .get()
                                .addOnSuccessListener { documents ->
                                    var questionsList = documents.toObjects(Question2::class.java)

                                    // 필터링된 질문 리스트에 없는 가족 구성원을 위한 Question2 객체 추가
                                    var additionalQuestions = familyNames.filter { name ->
                                        questionsList.none { question2 -> question2.name == name }
                                    }.map { name ->
                                        Question2(
                                            name = name,
                                            question = question?.question ?: "",
                                            answer = "등록안함"
                                        )
                                    }

                                    Log.e("dd", name)
                                    // userInfo[0].name과 일치하는 항목 제거
                                    var questionsList2 = questionsList.filter { it.name == name }
                                    var additionalQuestions2 =
                                        additionalQuestions.filter { it.name == name }
                                    var list = questionsList2 + additionalQuestions2
                                    questionsList = questionsList.filter { it.name != name }
                                    additionalQuestions =
                                        additionalQuestions.filter { it.name != name }

                                    Log.e("list", list.toString())
                                    nametxt.text = list[0].name
                                    answer.setText(list[0].answer)
                                    Log.e("dd", additionalQuestions.toString())
                                    // additionalQuestions을 questionsList에 추가
                                    questionsList = questionsList + additionalQuestions
                                    Log.e("dd", questionsList.toString())
                                    // 현재 질문과 일치하는 항목으로 필터링
                                    questionsList =
                                        questionsList.filter { it.question == question?.question }
                                    Log.e("dd", questionsList.toString())
                                    adapter.setQuestions(questionsList) // 어댑터에 업데이트된 리스트 적용
                                    adapter.notifyDataSetChanged() // RecyclerView에 데이터가 변경되었음을 알림
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("ddd", exception.toString())
                                }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("ddd", exception.toString())
                        }

// ... 기존 코드 ...

                }
                .addOnFailureListener { exception ->
                    Log.e("ddd", exception.toString())
                }



    }
    private fun saveAnswerToFirebase(name: String, question: String, answer: String, familycode: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // 문서 경로 설정

            // 데이터를 Firestore에 저장
            val answerMap = hashMapOf(
                "name" to name,
                "question" to question,
                "answer" to answer
            )

            firestore.collection("family")
                .document("family")
                .collection(familycode)
                .document(familycode)
                .collection("answer")
                .document(question)
                .set(answerMap)
                .addOnSuccessListener {
                    Log.d("QuestionActivity", "DocumentSnapshot successfully written!")

                    firestore.collection("family")
                        .document("detail")
                        .collection(familycode)
                        .document("exp")
                        .update("exp", FieldValue.increment(5)) // exp 필드의 값을 5만큼 증가
                        .addOnSuccessListener {
                            Log.d("Firestore", "exp successfully incremented.")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error incrementing exp", e)
                        }
                    Toast.makeText(this, "exp +5.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("QuestionActivity", "Error writing document", e)
                }
        } else {
            // 유저가 로그인하지 않은 경우
            Log.d("QuestionActivity", "User not logged in")
        }

    }
}
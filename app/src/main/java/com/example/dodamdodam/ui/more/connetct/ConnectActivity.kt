package com.example.dodamdodam.ui.more.connetct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dodamdodam.R
import com.example.dodamdodam.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConnectActivity : AppCompatActivity() {

    private lateinit var editTextFamilyCode: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        // Firebase 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // 뷰 바인딩
        editTextFamilyCode = findViewById(R.id.editTextUserInput)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // 버튼 클릭 리스너 설정
        buttonSubmit.setOnClickListener {
            val familyCode = editTextFamilyCode.text.toString().trim()
            if (familyCode.isNotEmpty()) {
                addFamilyMember(familyCode)
            } else {
                // 가족 코드가 입력되지 않았을 경우 사용자에게 알림
                Toast.makeText(this, "가족 코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addFamilyMember(familyCode: String) {
        val currentUser = firebaseAuth.currentUser
        firestore.collection(currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val userName = userInfo[0].name
                Log.e("ddd", userName)
                var familyMember = hashMapOf("email" to currentUser?.email, "name" to userName)
                firestore.collection("family")
                    .document("family")
                    .collection(familyCode)
                    .document(currentUser?.email?.dropLast(10).toString())
                    .set(familyMember)
                    .addOnSuccessListener {
                        // 데이터 저장 성공 시 사용자에게 알림
                        Toast.makeText(this, "가족 코드에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        firestore.collection(currentUser?.email.toString())
                            .document("userinfo")
                            .update("family", familyCode)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // 에러 처리
                        Toast.makeText(this, "가족 코드 추가에 실패했습니다: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            }

    }
}

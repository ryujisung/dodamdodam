package com.example.dodamdodam.ui.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityPoemBinding
import com.example.dodamdodam.model.Poem
import com.google.firebase.firestore.FirebaseFirestore

class PoemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPoemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_poem)

        // 인텐트에서 title 받아오기
        val title = intent.getStringExtra("poemId")

        // Firestore에서 해당 title의 Poem 데이터 가져오기
        FirebaseFirestore.getInstance().collection("poem")
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isNotEmpty()) {
                    // 데이터 바인딩을 통해 화면에 표시
                    val poem = documents.documents[0].toObject(Poem::class.java)
                    binding.poem = poem
                } else {
                    // 적절한 처리 (데이터가 없을 경우)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PoemActivity", "Error getting documents: ", exception)
            }
    }
}

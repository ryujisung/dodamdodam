package com.example.dodamdodam.ui.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityNovelBinding
import com.example.dodamdodam.model.Novel
import com.example.dodamdodam.model.Poem
import com.google.firebase.firestore.FirebaseFirestore

class NovelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNovelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_novel)

        // 인텐트에서 title 받아오기
        val title = intent.getStringExtra("novelId")

        // Firestore에서 해당 title의 Novel 데이터 가져오기
        FirebaseFirestore.getInstance().collection("novel")
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isNotEmpty()) {
                    // 데이터 바인딩을 통해 화면에 표시
                    var novel = documents.documents[0].toObject(Novel::class.java)
                    novel?.introduce = novel?.introduce!!.replace("newline", "\n")

                    binding.novel = novel
                } else {
                    // 적절한 처리 (데이터가 없을 경우)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("NovelActivity", "Error getting documents: ", exception)
            }
    }
}

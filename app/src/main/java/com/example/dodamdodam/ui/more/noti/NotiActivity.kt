package com.example.dodamdodam.ui.more.noti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Noti
import com.google.firebase.firestore.FirebaseFirestore

class NotiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noti)

        recyclerView = findViewById(R.id.notice_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        firestore = FirebaseFirestore.getInstance()

        loadNotices()
    }

    private fun loadNotices() {
        firestore.collection("notice")
            .get()
            .addOnSuccessListener { documents ->
                val notiList = documents.toObjects(Noti::class.java)
                val adapter = NotiAdapter(notiList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
            }
    }
}

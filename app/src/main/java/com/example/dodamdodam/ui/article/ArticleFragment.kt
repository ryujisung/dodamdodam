package com.example.dodamdodam.ui.article

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Novel
import com.example.dodamdodam.model.Poem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ArticleFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter: PoemAdapter
    private lateinit var adapter2: NovelAdapter
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.poem_recycle)
        recyclerView.layoutManager = LinearLayoutManager(context)
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("poem")
            .get()
            .addOnSuccessListener { documents ->
                val Poemlist = documents.toObjects(Poem::class.java)
                Log.e("ddd",Poemlist.toString())

                adapter = PoemAdapter(Poemlist)
                recyclerView.adapter = adapter

                recyclerView2 = view.findViewById(R.id.novel_recycle)
                recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            .addOnFailureListener { exception ->
                Log.e("ddd", exception.toString())
            }
        firestore.collection("novel")
            .get()
            .addOnSuccessListener { documents ->
                val Novellist = documents.toObjects(Novel::class.java)
                Log.e("ddd",Novellist.toString())

                recyclerView2 = view.findViewById(R.id.novel_recycle)
                recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


                adapter2 = NovelAdapter(Novellist)
                recyclerView2.adapter = adapter2
            }
            .addOnFailureListener { exception ->
                Log.e("ddd", exception.toString())
            }

    }
}
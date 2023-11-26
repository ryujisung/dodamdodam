package com.example.dodamdodam.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.dodamdodam.R
import com.example.dodamdodam.model.Exp
import com.example.dodamdodam.model.Question
import com.example.dodamdodam.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var imageView: ImageView? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.treeImageView) // 레이아웃 파일에 정의된 ImageView의 ID로 대체
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser
        firestore.collection(currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val familycode = userInfo[0].family
                firestore.collection("family")
                    .document("detail")
                    .collection(familycode)
                    .get()
                    .addOnSuccessListener { documents ->
                        var explist = documents.toObjects(Exp::class.java)
                        val exp = explist[0].exp
                        setImageForIndex(exp.toInt())

                    }
            }




    }

    private fun setImageForIndex(index: Int) {
        val imageResourceId = when (index / 10) {
            0 -> R.drawable.tree0
            1 -> R.drawable.tree1
            2 -> R.drawable.tree2
            3 -> R.drawable.tree3
            4 -> R.drawable.tree4
            5 -> R.drawable.tree5
            6 -> R.drawable.tree6
            7 -> R.drawable.tree7
            8 -> R.drawable.tree8
        // 여기에 다른 경우에 대한 처리를 추가할 수 있습니다.
            else -> R.drawable.tree0 // 기본 이미지
            }

    // 이미지 뷰에 이미지를 설정합니다.
        imageView?.setImageResource(imageResourceId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageView = null // 뷰가 파괴될 때 참조를 정리합니다.
    }
}
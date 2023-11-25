package com.example.dodamdodam.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.FragmentMoreBinding
import com.example.dodamdodam.model.Family
import com.example.dodamdodam.ui.base.BaseFragment
import com.example.dodamdodam.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        val email = user?.email // 현재 로그인한 사용자의 이메일
        firestore
            .collection("family")
            .document("family")
            .collection(email?.dropLast(10).toString())
            .get()
            .addOnSuccessListener { documents ->

                var friend = documents.toObjects(Family::class.java)
                var friendlist = ""
                for(i in friend.indices) {
                    friendlist += friend[i].name + "," + " "
                }
                binding.fammilys = friendlist.dropLast(2)
                binding.myTxtLogout.setOnClickListener {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }


        return view
    }
}

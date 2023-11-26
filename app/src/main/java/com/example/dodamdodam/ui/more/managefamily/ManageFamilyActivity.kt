package com.example.dodamdodam.ui.more.managefamily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityManageFamilyBinding
import com.example.dodamdodam.model.Family
import com.example.dodamdodam.model.FamilyMember
import com.example.dodamdodam.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageFamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageFamilyBinding
    private lateinit var familyMemberAdapter: FamilyMemberAdapter
    private lateinit var firestore: FirebaseFirestore
    private val familyMembers = listOf(
        FamilyMember("한선린", "010-1234-5678"),
        // 다른 가족 구성원 데이터 추가...
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_family)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        binding.familycode = user?.email?.dropLast(10).toString()
        firestore.collection(user?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val familycode = userInfo[0].family
                val name = userInfo[0].name
                firestore.collection("family")
                    .document("family")
                    .collection(familycode)
                    .get()
                    .addOnSuccessListener { document ->
                        val families = document.toObjects(Family::class.java)
                        val familyNames = families.map { it.name } // 가족 구성원의 이름 리스트
                        familyMemberAdapter = FamilyMemberAdapter(families)
                        binding.recyclerview.apply {
                            adapter = familyMemberAdapter
                            layoutManager = LinearLayoutManager(this@ManageFamilyActivity)
                        }
                    }
            }


    }
}

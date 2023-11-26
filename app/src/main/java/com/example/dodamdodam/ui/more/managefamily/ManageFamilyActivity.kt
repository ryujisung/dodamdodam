package com.example.dodamdodam.ui.more.managefamily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityManageFamilyBinding
import com.example.dodamdodam.model.FamilyMember

class ManageFamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageFamilyBinding
    private lateinit var familyMemberAdapter: FamilyMemberAdapter
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
        familyMemberAdapter = FamilyMemberAdapter(familyMembers)
        binding.recyclerview.apply {
            adapter = familyMemberAdapter
            layoutManager = LinearLayoutManager(this@ManageFamilyActivity)
        }
    }
}

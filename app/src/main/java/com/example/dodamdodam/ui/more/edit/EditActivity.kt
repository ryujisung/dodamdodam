package com.example.dodamdodam.ui.more.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityEditBinding
import com.example.dodamdodam.model.UserInfo
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditActivity : BaseActivity<ActivityEditBinding>(R.layout.activity_edit) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.txtPhone.setOnClickListener {
            val intent = Intent(this, EditPhoneActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        val email = user?.email
        firestore.collection(email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                binding.name = userInfo[0].name
                binding.email = userInfo[0].email
                binding.phone = userInfo[0].phone
            }

    }
}
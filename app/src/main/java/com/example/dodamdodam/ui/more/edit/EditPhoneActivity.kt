package com.example.dodamdodam.ui.more.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityEditBinding
import com.example.dodamdodam.databinding.ActivityEditPhoneBinding
import com.example.dodamdodam.model.UserInfo
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditPhoneActivity : BaseActivity<ActivityEditPhoneBinding>(R.layout.activity_edit_phone) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phone)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_phone)
        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        val email = user?.email
        binding.buttonSubmit.setOnClickListener {
            if (binding.editTextUserPhone.text.toString().isEmpty()) {
                return@setOnClickListener
            }
            firestore.collection(email.toString())
                .document("userinfo")
                .update("phone", binding.editTextUserPhone.text.toString())
                .addOnSuccessListener {
                    finish()
                }
        }


    }
}
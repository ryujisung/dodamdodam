package com.example.dodamdodam.ui.more.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityEditNameBinding
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditNameActivity : BaseActivity<ActivityEditNameBinding>(R.layout.activity_edit_name) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_name)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_name)
        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        val email = user?.email
        binding.buttonSubmit.setOnClickListener {
            if (binding.editTextUserName.text.toString().isEmpty()) {
                return@setOnClickListener
            }
            firestore.collection(email.toString())
                .document("userinfo")
                .update("name", binding.editTextUserName.text.toString())
        }


    }
}
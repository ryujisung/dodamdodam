package com.example.dodamdodam.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.FragmentMoreBinding
import com.example.dodamdodam.model.Family
import com.example.dodamdodam.model.UserInfo
import com.example.dodamdodam.ui.base.BaseFragment
import com.example.dodamdodam.ui.login.LoginActivity
import com.example.dodamdodam.ui.more.connetct.ConnectActivity
import com.example.dodamdodam.ui.more.edit.EditActivity
import com.example.dodamdodam.ui.more.managefamily.ManageFamilyActivity
import com.example.dodamdodam.ui.more.noti.NotiActivity
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
        firestore.collection(email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val userInfo = documents.toObjects(UserInfo::class.java)
                val familycode = userInfo[0].family
                Log.e("ddd", email.toString())
                firestore.collection("family")
                    .document("family")
                    .collection(familycode)
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
            }


        binding.myTxtEdit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }
        binding.myTxtConnect.setOnClickListener {
            val intent = Intent(context, ConnectActivity::class.java)
            startActivity(intent)
        }
        binding.myTxtLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.myTxtNoti2.setOnClickListener {
            val intent = Intent(context, NotiActivity::class.java)
            startActivity(intent)
        }
        binding.myLayoutFamilly.setOnClickListener {
            val intent = Intent(context, ManageFamilyActivity::class.java)
            startActivity(intent)
        }
        binding.myTxtCenter.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/me/dodamamdoodam"))
            startActivity(intent)
        }
        return view
    }
}

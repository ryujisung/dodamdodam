package com.example.dodamdodam.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityLoginBinding
import com.example.dodamdodam.ui.main.MainActivity
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        // Firebase 인증 객체의 인스턴스를 가져옵니다.
        auth = FirebaseAuth.getInstance()

        binding.btnStart.setOnClickListener {
            val email = binding.loginEdtEmail.text.toString().trim()
            val password = binding.loginEdtPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // 사용자 입력이 모두 채워져 있으면 로그인을 시도합니다.
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // 로그인 성공 시, MainActivity로 이동합니다.
                            val loginintent = Intent(this, MainActivity::class.java)
                            startActivity(loginintent)
                            finish()
                        } else {
                            // 로그인 실패 시, 에러 메시지를 표시합니다.
                            binding.txtSign1Eror.visibility = android.view.View.VISIBLE
                            binding.txtSign1Eror.text = "아이디 또는 비밀번호가 틀렸습니다."
                            binding.loginEdtEmail.setBackgroundResource(R.drawable.edt_error)
                            binding.loginEdtPassword.setBackgroundResource(R.drawable.edt_error)
                        }
                    }
            } else {
                // 이메일 또는 비밀번호 입력란이 비어 있으면 경고 메시지를 띄웁니다.
                binding.txtSign1Eror.text = "이메일과 비밀번호를 입력해주세요."
                binding.txtSign1Eror.visibility = android.view.View.VISIBLE
                binding.loginEdtEmail.setBackgroundResource(R.drawable.edt_error)
                binding.loginEdtPassword.setBackgroundResource(R.drawable.edt_error)
            }
        }

        binding.loginBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

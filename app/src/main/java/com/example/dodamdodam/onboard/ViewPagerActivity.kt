package com.example.dodamdodam.onboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dodamdodam.LoginActivity
import com.example.dodamdodam.R
import com.example.dodamdodam.signup.SignUpActivity
import com.example.dodamdodam.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding
    private lateinit var ViewPagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        // 뷰 바인딩, 어댑터 실행
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        initAdapter()

        setContentView(binding.root)

        // indicator 연결
        binding.dotsIndicator.setViewPager2(binding.boardVp)

        // 회원가입 버튼 눌렀을 때
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 로그인 버튼 눌렀을 때
        binding.btnLoginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun initAdapter() {
        //Adapter 안에 ViewPager2 상에 띄워줄 fragmentList 생성
        val fragmentList = listOf(Onboard1Fragment(),Onboard2Fragment(),Onboard3Fragment())

        //ViewPagerAdapter 초기화
        ViewPagerAdapter = PagerAdapter(this)
        ViewPagerAdapter.fragments.addAll(fragmentList)

        //ViewPager2와 Adapter 연동
        binding.boardVp.adapter = ViewPagerAdapter

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.boardVp.currentItem == 0) {
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            binding.boardVp.currentItem = binding.boardVp.currentItem - 1
        }
    }
}
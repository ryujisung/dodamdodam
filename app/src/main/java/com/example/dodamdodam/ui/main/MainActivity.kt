package com.example.dodamdodam.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityMainBinding
import com.example.dodamdodam.ui.article.ArticleFragment
import com.example.dodamdodam.ui.base.BaseActivity
import com.example.dodamdodam.ui.chat.ChatFragment
import com.example.dodamdodam.ui.home.HomeFragment
import com.example.dodamdodam.ui.more.MoreFragment
import com.example.dodamdodam.ui.quetion.QuetionFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bnv_main = binding.bnvMain

        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.main_menu_home -> {
                        // 다른 프래그먼트 화면으로 이동하는 기능
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, homeFragment).commit()
                    }
                    R.id.main_menu_chat -> {
                        val chatFragment = ChatFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, chatFragment).commit()
                    }
                    R.id.main_menu_article -> {
                        val articleFragment = ArticleFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, articleFragment).commit()
                    }
                    R.id.main_menu_quetion -> {
                        val quetionFragment = QuetionFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, quetionFragment).commit()
                    }
                    R.id.main_menu_my -> {
                        val moreFragment = MoreFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, moreFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.main_menu_home
        }

    }
}
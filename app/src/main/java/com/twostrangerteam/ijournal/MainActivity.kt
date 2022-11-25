package com.twostrangerteam.ijournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import com.twostrangerteam.ijournal.fragments.FragmentGrade
import com.twostrangerteam.ijournal.fragments.FragmentHomeWork
import com.twostrangerteam.ijournal.fragments.FragmentUser
import com.twostrangerteam.ijournal.joinus.LoginActivity

class MainActivity : AppCompatActivity() {
    //Биндинг
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инит биндинга
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        var openTab = intent.getStringExtra("TabName").toString()
        if (openTab == "null"){
            binding.bottomMenu.selectedItemId = R.id.homework_list
        }
        else if (openTab == "gradle"){
            binding.bottomMenu.selectedItemId = R.id.raiting_list
        }
        binding.bottomMenu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homework_list -> {supportFragmentManager.beginTransaction().replace(R.id.main_frame, FragmentHomeWork.newInstance()).commit()}
                R.id.raiting_list -> {supportFragmentManager.beginTransaction().replace(R.id.main_frame, FragmentGrade.newInstance()).commit()}
                R.id.user_list -> {supportFragmentManager.beginTransaction().replace(R.id.main_frame, FragmentUser.newInstance()).commit()}
            }
            true
        }

        verifyUser()
    }

    //проверка на вход
    private fun verifyUser() {
        val uid = FirebaseAuth.getInstance().uid

        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        else{
            var openTab = intent.getStringExtra("TabName").toString()
            if (openTab == "null"){
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frame, FragmentHomeWork.newInstance())
                    .commit()
            }
            else if (openTab == "gradle"){
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frame, FragmentGrade.newInstance())
                    .commit()
            }
        }
    }
}
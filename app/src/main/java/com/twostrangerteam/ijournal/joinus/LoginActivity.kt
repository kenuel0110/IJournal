package com.twostrangerteam.ijournal.joinus

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twostrangerteam.ijournal.MainActivity
import com.twostrangerteam.ijournal.R
import com.twostrangerteam.ijournal.databinding.ActivityLoginBinding
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import java.lang.StringBuilder

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инит биндинга
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_login)

        //градиент
        val paint = binding.textViewLogo5.paint
            val width = paint.measureText(binding.textViewLogo5.text.toString())
            val textShader: Shader = LinearGradient(0f,0f, width, binding.textViewLogo5.textSize, intArrayOf(
                Color.parseColor("#FFDD00"),
                Color.parseColor("#FF0000"),
                Color.parseColor("#00C4FF")
            ), null, Shader.TileMode.REPEAT)

            binding.textViewLogo5.paint.setShader(textShader)

        binding.btnLogin.setOnClickListener {
            signIn()
        }

        //на регистрацию
        binding.goToReg.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun signIn(){
        val email = binding.teEmail2.text.toString()
        val pass = binding.tePass2.text.toString()

        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Заполните поля!", Toast.LENGTH_SHORT).show()
            binding.btnLogin.isEnabled = true
            return
        }
        else{
            binding.btnLogin.isEnabled = false
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener{
                    if (!it.isSuccessful) return@addOnCompleteListener

                    makeToast()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun makeToast() {
        val mRefUser = FirebaseDatabase.getInstance().getReference("/users/")
        val uid = FirebaseAuth.getInstance().uid
        mRefUser.child(uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val nick = it.child("nick").value.toString()
                Toast.makeText(this, "Шалом, $nick", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
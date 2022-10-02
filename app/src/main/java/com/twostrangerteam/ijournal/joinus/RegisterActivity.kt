package com.twostrangerteam.ijournal.joinus

import android.R.attr
import android.R.attr.*
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.twostrangerteam.ijournal.MainActivity
import com.twostrangerteam.ijournal.classes.User
import com.twostrangerteam.ijournal.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //инит биндинга
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_login)

        //градиент
        val paint = binding.textViewLogo1.paint
        val width = paint.measureText(binding.textViewLogo1.text.toString())
        val textShader: Shader = LinearGradient(0f,0f, width, binding.textViewLogo1.textSize, intArrayOf(
            Color.parseColor("#FFDD00"),
            Color.parseColor("#FF0000"),
            Color.parseColor("#00C4FF")
        ), null, Shader.TileMode.REPEAT)

        binding.textViewLogo1.paint.setShader(textShader)

        //регистрация
        binding.btnReg.setOnClickListener {
            register()
        }

        //фотокарточка
        binding.buttonPhotoCh.setOnClickListener{
            setImage()
        }

        //на вход
        binding.goToLogin.setOnClickListener {
            finish()
        }

    }

    private fun setImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

        //кроп
        /*CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
*/
        //startCropActivity()

    }

    private fun startCropActivity() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
    }

    //Картинка
    var selectedPhotoUri: Uri? = null

    //Получение картинки
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null){
            selectedPhotoUri = data.data
            Toast.makeText(this, selectedPhotoUri.toString(), Toast.LENGTH_SHORT)

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //val bitmapDrawable = BitmapDrawable(bitmap)
            binding.selectIv.setImageBitmap(bitmap)
            binding.buttonPhotoCh.alpha = 0f

        }

       /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                selectedPhotoUri = result.uri
                Toast.makeText(this, selectedPhotoUri.toString(), Toast.LENGTH_SHORT)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                //val bitmapDrawable = BitmapDrawable(bitmap)
                binding.selectIv.setImageBitmap(bitmap)
                binding.buttonPhotoCh.alpha = 0f
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private fun register(){
        val nick = binding.teNick.text.toString()
        val crypt = binding.teCrypt.text.toString()
        val email = binding.teEmail.text.toString()
        val pass = binding.tePass.text.toString()

        if(email.isEmpty() || pass.isEmpty() || crypt.isEmpty() || nick.isEmpty()){
            Toast.makeText(this, "Заполните поля!", Toast.LENGTH_SHORT).show()
            binding.btnReg.isEnabled = true
            return
        }

        else{
            binding.btnReg.isEnabled = false
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener{
                    if (!it.isSuccessful) return@addOnCompleteListener
                    uploadImage2FirebaseSge()
                }
                .addOnFailureListener {
                    binding.btnReg.isEnabled = true
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadImage2FirebaseSge() {
        if(selectedPhotoUri == null){
            saveUser2DB("https://firebasestorage.googleapis.com/v0/b/ijournal-fb041.appspot.com/o/images%2FiMAGE_NOT_FIND.png?alt=media&token=1e1b398d-fc17-458e-b98b-ef45a3ad476e")
        }
        else if(selectedPhotoUri != null){
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener{
                    saveUser2DB(it.toString())
                }
            }
        }
    }

    private fun saveUser2DB(profileImageUrl: String) {
        val nick = binding.teNick.text.toString()
        val crypt = binding.teCrypt.text.toString()
        val email = binding.teEmail.text.toString()

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, nick, profileImageUrl, crypt, email)

        ref.setValue(user).addOnSuccessListener {
            Toast.makeText(this, "Добро пожаловать, $nick", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}

//class User (val uid: String, val nick: String, val profileImageUrl: String, val crypt: String, val email: String)

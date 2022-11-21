package com.twostrangerteam.ijournal.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.twostrangerteam.ijournal.databinding.FragmentUserBinding
import com.twostrangerteam.ijournal.joinus.LoginActivity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.InputType
import android.widget.EditText
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.twostrangerteam.ijournal.*
import com.twostrangerteam.ijournal.classes.User
import java.util.*


class FragmentUser : Fragment() {

    lateinit var binding: FragmentUserBinding

    private var nick = ""
    private var uid = ""
    private var crypt = ""
    private var email = ""
    private var selectedPhoto = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInformation()

        binding.btnSignOut.setOnClickListener {
            signOut()
        }

        binding.avatarka.setOnClickListener {
            changeAvatar()
        }
        binding.tvNick.setOnClickListener {
            dataChange("Изменение ника", nick, "Введите новый ник", InputType.TYPE_CLASS_TEXT, "nick")
        }
        binding.tvCrypt.setOnClickListener {
            dataChange("Изменение шифра студака", crypt, "Введите новый шифр", InputType.TYPE_CLASS_NUMBER, "crypt")
        }
        binding.tvEmail.setOnClickListener {
            dataChange("Изменение Em@il", email, "Введите новый Em@il", InputType.TYPE_CLASS_TEXT, "email")
        }
        binding.btnChat.setOnClickListener {
            Toast.makeText(activity, "Ещё в разработке", Toast.LENGTH_SHORT).show()
        }
        binding.btnPlaning.setOnClickListener {
            val intent = Intent(view.context, SheduleActivity::class.java)
            startActivity(intent)
        }
        binding.btnBallList.setOnClickListener {
            val intent = Intent(view.context, BallListActivity::class.java)
            startActivity(intent)
        }
        binding.btnSettings.setOnClickListener {
            val intent = Intent(view.context, Settings_ListActivity::class.java)
            startActivity(intent)
        }

        binding.btnCabinets.setOnClickListener {
            val intent = Intent(view.context, CabinetsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun dataChange(title: String, value: String, hint: String, type: Int, name_v: String) {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(getActivity())
        builder.setTitle(title)

        val input = EditText(getActivity())
        input.setHint(hint)
        input.setText(value)
        input.inputType = type
        builder.setView(input)
        builder.setPositiveButton("Изменить", DialogInterface.OnClickListener { dialog, which ->
            when(name_v){
                "nick"->{
                    nick= input.text.toString()
                }
                "email"->{
                    email= input.text.toString()
                }
                "crypt"->{
                    crypt= input.text.toString()
                }
            }
            if (value != input.text.toString()) changeUserData()
        })
        builder.setNegativeButton("Отменить", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun changeUserData() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val mRefUser = FirebaseDatabase.getInstance().getReference("/users/")
        mRefUser.child(uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val user = User(
                    uid,
                    nick,
                    selectedPhoto,
                    crypt,
                    email)

                ref.setValue(user).addOnSuccessListener{
                    Toast.makeText(activity, "Профиль изменён", Toast.LENGTH_SHORT).show()
                    getUserInformation()
                }.addOnFailureListener {
                    Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeAvatar() {

            val builder = AlertDialog.Builder(getActivity())
            builder.setTitle("Вы уверены, что хотите изменить аватар?")
            builder.setPositiveButton("Да",{dialogInterface, i ->
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)

            })
            builder.setNegativeButton("Нет",{dialog, i -> })
            builder.show()
        }


    var selectedPhotoUri: Uri? = null

    //Получение картинки
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            selectedPhotoUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), selectedPhotoUri)
            binding.avatarka.setImageBitmap(bitmap)
            updatePhoto()
    }

    private fun updatePhoto() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener{
                updateUserPhoto2DB(it.toString())
            }
        }
    }

    private fun updateUserPhoto2DB(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val mRefUser = FirebaseDatabase.getInstance().getReference("/users/")
        mRefUser.child(uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val user = User(
                    uid,
                    it.child("nick").value.toString(),
                    profileImageUrl,
                    it.child("crypt").value.toString(),
                    it.child("email").value.toString())

                ref.setValue(user).addOnSuccessListener{
                    Toast.makeText(activity, "Фото профиля сохранено", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root

    }

    private fun signOut() {
        //Toast.makeText(getActivity(), "yfkjdhfslkdhflksjhfksldjfh", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(getActivity(), LoginActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        getActivity()?.startActivity(intent)
    }


    private fun getUserInformation() {
        val mRefUser = FirebaseDatabase.getInstance().getReference("/users/")
        uid = FirebaseAuth.getInstance().uid.toString()
        mRefUser.child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                nick = it.child("nick").value.toString()
                crypt = it.child("crypt").value.toString()
                email = it.child("email").value.toString()
                selectedPhoto = it.child("profileImageUrl").value.toString()
                Picasso.get().load(selectedPhoto).into(binding.avatarka)
                binding.tvNick.text = nick
                binding.tvCrypt.text = crypt
                binding.tvEmail.text = email
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentUser()
    }
}
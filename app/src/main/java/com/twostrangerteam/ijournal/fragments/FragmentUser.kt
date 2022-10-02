package com.twostrangerteam.ijournal.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.twostrangerteam.ijournal.MainActivity
import com.twostrangerteam.ijournal.R
import com.twostrangerteam.ijournal.databinding.ActivityMainBinding
import com.twostrangerteam.ijournal.databinding.FragmentUserBinding
import com.twostrangerteam.ijournal.joinus.LoginActivity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.twostrangerteam.ijournal.classes.User
import java.util.*


class FragmentUser : Fragment() {

    lateinit var binding: FragmentUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInformation()

        binding.btnSignOut.setOnClickListener {
            signOut()
        }

        binding.avatarka.setOnClickListener {
            changeAvatar()
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
        val uid = FirebaseAuth.getInstance().uid
        mRefUser.child(uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                Picasso.get().load(it.child("profileImageUrl").value.toString()).into(binding.avatarka)
                binding.tvNick.text = it.child("nick").value.toString()
                binding.tvCrypt.text = it.child("crypt").value.toString()
                binding.tvEmail.text = it.child("email").value.toString()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentUser()
    }
}
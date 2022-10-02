package com.twostrangerteam.ijournal.fragments

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
import com.squareup.picasso.Picasso


class FragmentUser : Fragment() {

    lateinit var binding: FragmentUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInformation()

        binding.btnSignOut.setOnClickListener {
            signOut()
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
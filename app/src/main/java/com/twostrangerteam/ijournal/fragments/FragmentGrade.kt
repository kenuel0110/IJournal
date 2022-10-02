package com.twostrangerteam.ijournal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twostrangerteam.ijournal.R
import com.twostrangerteam.ijournal.databinding.FragmentGradeBinding
import com.twostrangerteam.ijournal.databinding.FragmentUserBinding


class FragmentGrade : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGradeBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentGrade()
    }
}
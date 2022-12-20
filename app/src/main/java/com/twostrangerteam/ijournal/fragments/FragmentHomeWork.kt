package com.twostrangerteam.ijournal.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twostrangerteam.ijournal.HomeWADDActivity
import com.twostrangerteam.ijournal.HomeWPageActivity
import com.twostrangerteam.ijournal.MainActivity
import com.twostrangerteam.ijournal.R
import com.twostrangerteam.ijournal.classes.HomeWorkItem
import com.twostrangerteam.ijournal.classes.HomeWorkModel
import com.twostrangerteam.ijournal.databinding.FragmentHomeWorkBinding
import com.twostrangerteam.ijournal.joinus.LoginActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragment_home_work.*

class FragmentHomeWork : Fragment() {

    private var types: ArrayList<String> = arrayListOf(
        "Фильтр",
        "Экология",
        "Информационные технологии",
        "Интернет технологии",
        "Культура речи",
        "Английский",
        "Математика",
        "Физ-ра",
        "Введение в профессию",
        "История",
        "Проектная деятельность"
    )
    lateinit var binding: FragmentHomeWorkBinding
    lateinit var adapter: SpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types,
            R.layout.spinner_list
        ) as SpinnerAdapter
        binding.spinnerFilter.adapter = adapter

        binding.swipetorefreshHw.isRefreshing = true
        initSpinner()
        binding.swipetorefreshHw.isRefreshing = false

        binding.btnAddHomeWork.setOnClickListener {
            val intent = Intent(activity, HomeWADDActivity::class.java)
            startActivity(intent)
        }

        binding.swipetorefreshHw.setOnRefreshListener {
            var filterId = binding.spinnerFilter.selectedItemId.toString().toInt()
            initSpinner()
            binding.spinnerFilter.setSelection(filterId)
            binding.swipetorefreshHw.isRefreshing = false
        }
    }

    private fun initSpinner() {

        /* //создание объекта свайпа и запуск функции при удачном исходе
        val item = object: SwipeToDelete(requireActivity().baseContext, 0, ItemTouchHelper.LEFT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteData(viewHolder.adapterPosition, adapter)
            }
        }

        //соединение "свайпа" и списка
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.rcHomeWork)*/

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (types[position] == "Фильтр") {
                    rc_homeWork.visibility = View.VISIBLE
                    tv_listEmpty.visibility = View.GONE
                    val mRefProducts = FirebaseDatabase.getInstance().getReference("/home_work/")
                        .orderByChild("data_end")
                    //слушатель данных
                    mRefProducts.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            //адаптер для рицайклвью
                            val adapter = GroupAdapter<ViewHolder>()
                            p0.children.forEach {
                                val hw = it.getValue(HomeWorkModel::class.java)
                                //добавление заполненого шаблона в рицайклвью
                                if (hw != null) {
                                    adapter.add(HomeWorkItem(hw))
                                }
                            }
                            //"спавн" шаблона
                            binding.rcHomeWork.layoutManager = LinearLayoutManager(requireContext())
                            //действие по нажатию
                            adapter.setOnItemClickListener { item, view ->

                                val hw = item as HomeWorkItem

                                val intent = Intent(view.context, HomeWPageActivity::class.java)
                                intent.putExtra(HOMEWORK_KEY, item.hw.uuid_hw)
                                startActivity(intent)
                            }
                            //подсоединение адаптера к recyclerview
                            binding.rcHomeWork.adapter = adapter
                            adapter.notifyDataSetChanged()
                            if (binding.rcHomeWork.getAdapter()?.getItemCount() == 0) {
                                rc_homeWork.visibility = View.GONE
                                tv_listEmpty.visibility = View.VISIBLE
                                //Toast.makeText(context, "Список пуст", Toast.LENGTH_SHORT).show()
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                } else {
                    rc_homeWork.visibility = View.VISIBLE
                    tv_listEmpty.visibility = View.GONE
                    val mRefProducts = FirebaseDatabase.getInstance().getReference("/home_work/")
                        .orderByChild("data_end")
                    //слушатель данных
                    mRefProducts.addListenerForSingleValueEvent(object : ValueEventListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDataChange(p0: DataSnapshot) {
                            //адаптер для рицайклвью
                            val adapter = GroupAdapter<ViewHolder>()
                            p0.children.forEach {
                                val hw = it.getValue(HomeWorkModel::class.java)
                                //добавление заполненого шаблона в рицайклвью
                                if (hw != null && hw.lesson == types[position]) {
                                    adapter.add(HomeWorkItem(hw))
                                }
                            }
                            //"спавн" шаблона
                            binding.rcHomeWork.layoutManager = LinearLayoutManager(requireContext())
                            //действие по нажатию
                            adapter.setOnItemClickListener { item, view ->

                                val hw = item as HomeWorkItem

                                val intent = Intent(view.context, HomeWPageActivity::class.java)
                                intent.putExtra(HOMEWORK_KEY, item.hw.uuid_hw)
                                startActivity(intent)
                            }
                            //подсоединение адаптера к рицайклвью
                            binding.rcHomeWork.adapter = adapter
                            adapter.notifyDataSetChanged()
                            if (binding.rcHomeWork.getAdapter()?.getItemCount() == 0) {
                                rc_homeWork.visibility = View.GONE
                                tv_listEmpty.visibility = View.VISIBLE
                                //Toast.makeText(context, "Список пуст", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeWorkBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHomeWork()
        val HOMEWORK_KEY = "HOMEWORK_KEY"
    }

}
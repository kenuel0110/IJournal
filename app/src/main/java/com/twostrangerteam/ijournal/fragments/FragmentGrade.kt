package com.twostrangerteam.ijournal.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twostrangerteam.ijournal.*
import com.twostrangerteam.ijournal.classes.GradleItem
import com.twostrangerteam.ijournal.classes.GradleModel
import com.twostrangerteam.ijournal.databinding.FragmentGradeBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class FragmentGrade : Fragment() {

    private var types: ArrayList<String> = arrayListOf("Фильтр", "Экология", "Информационные технологии", "Интернет технологии", "Культура речи", "Английский", "Математика", "Физ-ра", "Введение в профессию", "История", "Проектная деятельность")
    lateinit var binding: FragmentGradeBinding
    lateinit var adapter_spin: SpinnerAdapter
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.item_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()


        binding.rvGradle.layoutManager = GridLayoutManager(requireActivity().baseContext, 2)
        adapter_spin = ArrayAdapter.createFromResource(requireActivity().baseContext, R.array.types, R.layout.spinner_list) as SpinnerAdapter
        binding.spinnerGradleFilter.adapter = adapter_spin

        dialog.show()
        binding.swipetorefreshGr.isRefreshing = true
        initSpinner()
        binding.swipetorefreshGr.isRefreshing = false
        dialog.dismiss()

        binding.btnAddGradle.setOnClickListener {
            val intent = Intent(activity, GradeItemEditActivity::class.java)
            startActivity(intent)
        }
        binding.swipetorefreshGr.setOnRefreshListener{
            var filterId = binding.spinnerGradleFilter.selectedItemId.toString().toInt()
            initSpinner()
            binding.spinnerGradleFilter.setSelection(filterId)
            binding.swipetorefreshGr.isRefreshing = false
        }

    }

    private fun initSpinner() {
        adapter.clear()
        /* //создание объекта свайпа и запуск функции при удачном исходе
         val item = object: SwipeToDelete(requireActivity().baseContext, 0, ItemTouchHelper.LEFT){
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 deleteData(viewHolder.adapterPosition, adapter)
             }
         }

         //соединение "свайпа" и списка
         val itemTouchHelper = ItemTouchHelper(item)
         itemTouchHelper.attachToRecyclerView(binding.rcHomeWork)*/

        binding.spinnerGradleFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                adapter.clear()
                if (types[position] == "Фильтр"){
                    binding.rvGradle.visibility = View.VISIBLE
                    binding.tvListEmptyGradle.visibility = View.GONE
                    val mRefProducts = FirebaseDatabase.getInstance().getReference("/grades/").orderByChild("gradle_date")
                    //слушатель данных
                    mRefProducts.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            p0.children.forEach {
                                val gr = it.getValue(GradleModel::class.java)
                                //добавление заполненого шаблона в рицайклвью
                                if(gr != null && gr.uuid_user == FirebaseAuth.getInstance().uid.toString()){
                                    adapter.add(GradleItem(gr))
                                }
                            }
                            //действие по нажатию
                            adapter.setOnItemClickListener { item, view ->

                                val gr = item as GradleItem
                                deleteItem(gr.grad.uuid_gr.toString())
                            }
                            //подсоединение адаптера к recyclerview
                            binding.rvGradle.adapter = adapter
                            adapter.notifyDataSetChanged()
                            if(binding.rvGradle.getAdapter()?.getItemCount() == 0){
                                binding.rvGradle.visibility = View.GONE
                                binding.tvListEmptyGradle.visibility = View.VISIBLE
                                //Toast.makeText(context, "Список пуст", Toast.LENGTH_SHORT).show()
                            }

                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }


                else {
                    binding.rvGradle.visibility = View.VISIBLE
                    binding.tvListEmptyGradle.visibility = View.GONE
                    val mRefProducts = FirebaseDatabase.getInstance().getReference("/grades/").orderByChild("data")
                    //слушатель данных
                    mRefProducts.addListenerForSingleValueEvent(object: ValueEventListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDataChange(p0: DataSnapshot) {
                            p0.children.forEach {
                                val gr = it.getValue(GradleModel::class.java)
                                //добавление заполненого шаблона в рицайклвью
                                if(gr != null && gr.gradle_lesson == types[position].toString() && gr.uuid_user == FirebaseAuth.getInstance().uid.toString()){
                                    adapter.add(GradleItem(gr))
                                }
                            }
                            //действие по нажатию
                            adapter.setOnItemClickListener { item, view ->

                                val gr = item as GradleItem
                                deleteItem(gr.grad.uuid_gr.toString())
                            }
                            //подсоединение адаптера к рицайклвью
                            binding.rvGradle.adapter = adapter
                            adapter.notifyDataSetChanged()
                            if(binding.rvGradle.getAdapter()?.getItemCount() == 0){
                                binding.rvGradle.visibility = View.GONE
                                binding.tvListEmptyGradle.visibility = View.VISIBLE
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

    private fun deleteItem(uuid_hw: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Вы уверены, что хотите удалить оценку?")
        builder.setMessage("Это действие нельзя отменить")
        builder.setPositiveButton("Да",{dialogInterface, i ->
            val db = FirebaseDatabase.getInstance().getReference("/grades/").child(uuid_hw)
            val delete = db.removeValue()

            delete.addOnSuccessListener {
                Toast.makeText(requireContext(), "Оценка удалена", Toast.LENGTH_SHORT).show()
                initSpinner()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Нет",{dialog, i -> })
        builder.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGradeBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentGrade()
        val GRADE_KEY = "GRADE_KEY"
    }
}
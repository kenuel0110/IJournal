package com.twostrangerteam.ijournal.classes

import android.view.animation.AnimationUtils
import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_gradle.view.*

open class GradleItem (val grad: GradleModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.rv_gradle_cardview.animation = AnimationUtils.loadAnimation(viewHolder.itemView.rv_gradle_cardview.context, R.anim.item_multi_colomn)


        viewHolder.itemView.tv_title_gradle.text = grad.gradle_title
        viewHolder.itemView.tv_lesson_gradle.text = grad.gradle_lesson
        viewHolder.itemView.tv_data_gradle.text = grad.gradle_date
    }

    override fun getLayout(): Int {
        return R.layout.item_gradle
    }
}
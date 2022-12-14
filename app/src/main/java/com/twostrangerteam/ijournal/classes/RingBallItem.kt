package com.twostrangerteam.ijournal.classes

import android.view.animation.AnimationUtils
import com.twostrangerteam.ijournal.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.rv_ball_list_item.view.*

class RingBallItem(val rbi: RingBallModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.rv_bell_cardview.animation = AnimationUtils.loadAnimation(viewHolder.itemView.rv_bell_cardview.context, R.anim.item_alpha)

        viewHolder.itemView.tv_num_cab.text = rbi.lesson_num
        viewHolder.itemView.tv_name.text = rbi.timeStart_End
        viewHolder.itemView.id_time_break.text = rbi.breakTime
    }

    override fun getLayout(): Int {
        return R.layout.rv_ball_list_item
    }


}








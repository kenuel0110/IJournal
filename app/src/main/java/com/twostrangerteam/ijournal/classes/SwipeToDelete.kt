package com.xwray.groupie.example.item

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import com.twostrangerteam.ijournal.classes.GradleItem
import com.twostrangerteam.ijournal.classes.GradleModel

class SwipeToDelete(gradleModel: GradleModel) : GradleItem(gradleModel) {

    override fun getSwipeDirs(): Int {
        return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}
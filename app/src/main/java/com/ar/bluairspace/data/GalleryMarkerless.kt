package com.ar.bluairspace.data

import com.bluairspace.sdk.model.MarkerlessExperience

object GalleryMarkerless{
    private val maxCount = 5

    private var mList = listOf<MarkerlessExperience>()
    private var mListSelected = mutableListOf<MarkerlessExperience>()

    var list: List<MarkerlessExperience>
        get() = mList
        set(list) {
            mList = list
            mListSelected.clear()
        }

    fun markSelected(position: Int) {
        val me: MarkerlessExperience = mList[position]
        if (me.isSelected) {
            me.isSelected = false
            mListSelected.remove(me)
        } else {
            me.isSelected = true
            mListSelected.add(me)
            if (mListSelected.size > maxCount) {
                val meRemoved: MarkerlessExperience = mListSelected.removeAt(0)
                meRemoved.isSelected = false
            }
        }
    }

    fun unMarkSelected() {
        mListSelected.forEach { it.isSelected = false }
        mListSelected.clear()
    }

    fun clear() {
        mList = listOf()
        mListSelected.clear()
    }

    val countSelected: Int
        get() = mListSelected.size

    val listSelected: List<MarkerlessExperience>
        get() = mListSelected

}
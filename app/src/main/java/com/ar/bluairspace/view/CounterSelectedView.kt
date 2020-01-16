package com.ar.bluairspace.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.ar.bluairspace.R

class CounterSelectedView : LinearLayout {

    private var mText: TextView? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_counter_selected_items, this)
        mText = view.findViewById(R.id.txt_text)
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }

    fun setCount(count: Int) {
        if (count > 0) {
            mText!!.text = String.format("View in room (%d)", count)
            if (visibility == View.GONE) show()
        } else {
            mText!!.text = "Select items"
        }
    }
}
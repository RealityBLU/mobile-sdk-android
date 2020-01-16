package com.ar.bluairspace.fragment.markerless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ar.bluairspace.R
import com.ar.bluairspace.activity.AbstractActivity
import com.bluairspace.sdk.helper.Blu.startMarkerless
import com.bluairspace.sdk.helper.callback.DataCallback
import com.bluairspace.sdk.helper.callback.TaskCallback
import com.bluairspace.sdk.helper.data.BluDataHelper.getMarkerlessExperiences
import com.bluairspace.sdk.model.MarkerlessExperience
import com.bluairspace.sdk.util.PermissionUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ar.bluairspace.fragment.AbstractFragment
import com.ar.bluairspace.view.CounterSelectedView
import com.ar.bluairspace.data.GalleryMarkerless
import com.ar.bluairspace.data.GalleryMarkerless.countSelected
import com.ar.bluairspace.data.GalleryMarkerless.list
import com.ar.bluairspace.data.GalleryMarkerless.listSelected
import com.ar.bluairspace.data.GalleryMarkerless.markSelected
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter

class MarkerlessItemFragment : AbstractFragment() {

    private lateinit var mGrid: StickyGridHeadersGridView
    private lateinit var mCounterView: CounterSelectedView

    private lateinit var mAdapter: GridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mGrid = view.findViewById(R.id.grid_item)
        mCounterView = view.findViewById(R.id.counter_view)

        mCounterView.setOnClickListener {
            if (countSelected > 0) {
                (activity as AbstractActivity).startMarkerless(listSelected)
            }
        }

        mGrid.setAreHeadersSticky(false)
        mGrid.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ -> selectItem(position) }
        mAdapter = GridAdapter()
        mGrid.adapter = mAdapter
        updateData()
        updateCounterView()
    }

    private fun updateData() {
        if (list.isEmpty()) {
            val sceneId = arguments!!.getInt(EXTRA_ID_SCENE, -1)
            (activity as AbstractActivity?)!!.loadingView.showLoadingIndicator()
            getMarkerlessExperiences(sceneId, object : DataCallback<MarkerlessExperience> {
                override fun onSuccess(list: List<MarkerlessExperience>) {
                    GalleryMarkerless.list = list
                    try {
                        mAdapter.notifyDataSetChanged()
                        (activity as AbstractActivity?)!!.loadingView.hideLoadingIndicator()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFail(errorMessage: String) {
                    (activity as AbstractActivity?)!!.loadingView.showError(errorMessage)
                }
            })
        }
    }

    private fun selectItem(position: Int) {
        markSelected(position)
        mAdapter.notifyDataSetChanged()
        updateCounterView()
    }

    private fun updateCounterView() {
        mCounterView.setCount(countSelected)
    }

    override val layoutId: Int
        get() = R.layout.activity_markerless_exp_group

    inner class GridAdapter : BaseAdapter(), StickyGridHeadersSimpleAdapter {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var currentView = view
            val holder: ViewHolder
            if (currentView != null) {
                holder = currentView.tag as ViewHolder
            } else {
                val inflater = LayoutInflater.from(parent.context)
                currentView = inflater.inflate(R.layout.item_markerless_exp_in_group, parent, false)
                holder = ViewHolder(currentView)
                currentView.tag = holder
            }
            val me = list[position]
            holder.mDescription!!.text = me.name
            if (me.isSelected) {
                holder.mViewSelected!!.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_checkbox_on))
            } else {
                holder.mViewSelected!!.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_checkbox_off))
            }
            holder.mViewSelected!!.visibility = View.VISIBLE
            Glide.with(context!!).load(me.icon)
                .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                .into(holder.mThumbnail!!)
            return currentView!!
        }

        override fun getHeaderId(position: Int): Long {
            return list[position].subGroupId.toLong()
        }

        override fun getHeaderView(position: Int, view: View?, parent: ViewGroup): View {
            var currentView = view
            val holder: HeaderViewHolder
            if (currentView != null) {
                holder = currentView.tag as HeaderViewHolder
            } else {
                val inflater = LayoutInflater.from(parent.context)
                currentView = inflater.inflate(R.layout.item_markerless_group, parent, false)
                holder = HeaderViewHolder(currentView)
                currentView.tag = holder
            }
            val me = list[position]
            holder.mDescription.text = me.subGroup
            return currentView!!
        }

        internal inner class HeaderViewHolder(view: View?) {
            var mDescription: TextView = view!!.findViewById(R.id.txt_description)
        }

        internal inner class ViewHolder(view: View?) {
            var mThumbnail: ImageView? = view!!.findViewById(R.id.image_thumbnail)
            var mDescription: TextView? = view!!.findViewById(R.id.txt_description)
            var mViewSelected: View? = view!!.findViewById(R.id.view_selected_on)
        }
    }

    companion object {
        const val EXTRA_ID_SCENE = "EXTRA_ID_SCENE"
    }
}
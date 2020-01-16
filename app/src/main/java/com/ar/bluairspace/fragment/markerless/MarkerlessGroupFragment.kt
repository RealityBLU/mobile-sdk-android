package com.ar.bluairspace.fragment.markerless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.bluairspace.R
import com.bluairspace.sdk.helper.callback.DataCallback
import com.bluairspace.sdk.helper.data.BluDataHelper.getMarkerlessGroups
import com.bluairspace.sdk.model.MarkerlessGroup
import com.ar.bluairspace.activity.AbstractActivity
import com.ar.bluairspace.activity.MainActivity
import com.ar.bluairspace.data.GalleryMarkerless
import com.ar.bluairspace.fragment.AbstractFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MarkerlessGroupFragment : AbstractFragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MarkerlessExpAdapter

    override val layoutId: Int
        get() = R.layout.fragment_markerless_scene

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerView = view.findViewById(R.id.list_item)

        (activity as AbstractActivity?)!!.loadingView.showLoadingIndicator()
        getMarkerlessGroups(object : DataCallback<MarkerlessGroup> {
            override fun onSuccess(list: List<MarkerlessGroup>) {
                mAdapter = MarkerlessExpAdapter(list)
                mRecyclerView.layoutManager = LinearLayoutManager(activity)
                mRecyclerView.adapter = mAdapter
                (activity as AbstractActivity?)!!.loadingView.hideLoadingIndicator()
            }

            override fun onFail(errorMessage: String) {
                (activity as AbstractActivity?)!!.loadingView.showError(errorMessage)
            }
        })
    }

    inner class MarkerlessExpAdapter(private val mList: List<MarkerlessGroup>) :
        RecyclerView.Adapter<MarkerlessExpAdapter.MarkerlessExpHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerlessExpHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.item_markerless_scene, parent, false)
            return MarkerlessExpHolder(itemView)
        }

        override fun onBindViewHolder(holder: MarkerlessExpHolder, position: Int) {
            val markerlessGroup = mList[position]
            holder.bind(markerlessGroup)
            holder.itemView.tag = markerlessGroup
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        inner class MarkerlessExpHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var mThumbnail: ImageView? = itemView.findViewById(R.id.image_thumbnail)
            var mDescription: TextView? = itemView.findViewById(R.id.txt_description)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(markerlessScene: MarkerlessGroup) {
                mDescription!!.text = markerlessScene.title
                Glide.with(context!!)
                    .load(markerlessScene.icon)
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                    .into(mThumbnail!!)
            }

            override fun onClick(v: View) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    GalleryMarkerless.clear()
                    val fragment: Fragment = MarkerlessItemFragment()
                    val bundle = Bundle()
                    bundle.putInt(MarkerlessItemFragment.EXTRA_ID_SCENE, mList[position].id!!)
                    fragment.arguments = bundle
                    (activity as MainActivity?)!!.changeFragment(
                        fragment
                    )
                }
            }
        }

    }
}
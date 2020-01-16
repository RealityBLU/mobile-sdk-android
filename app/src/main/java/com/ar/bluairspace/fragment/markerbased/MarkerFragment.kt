package com.ar.bluairspace.fragment.markerbased

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.bluairspace.R
import com.bluairspace.sdk.helper.callback.DataCallback
import com.bluairspace.sdk.helper.data.BluDataHelper
import com.bluairspace.sdk.model.MarkerbasedMarker
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ar.bluairspace.activity.AbstractActivity
import com.ar.bluairspace.fragment.AbstractFragment

class MarkerFragment : AbstractFragment() {

    protected lateinit var mRecyclerView: RecyclerView
    private var mAdapter: MarkerlessAdapter? = null
    override val layoutId: Int
        get() = R.layout.fragment_download_marker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerView = view.findViewById(R.id.list_item)
        val orientation = activity!!.resources.configuration.orientation
        if (orientation == 1) mRecyclerView.layoutManager = LinearLayoutManager(activity)
        else mRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        (activity as AbstractActivity).loadingView.showLoadingIndicator()

        BluDataHelper.getMarkerbasedMarkers(object : DataCallback<MarkerbasedMarker> {
            override fun onSuccess(list: List<MarkerbasedMarker>) {
                mAdapter = MarkerlessAdapter(list)
                mRecyclerView.adapter = mAdapter
                (activity as AbstractActivity?)!!.loadingView.hideLoadingIndicator()
            }

            override fun onFail(errorMessage: String) {
                (activity as AbstractActivity?)!!.loadingView.showError(errorMessage)
            }
        })
    }

    inner class MarkerlessAdapter(private val mList: List<MarkerbasedMarker>) : RecyclerView.Adapter<MarkerlessAdapter.MarkerlessExpHolder?>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerlessExpHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.item_marker, parent, false)
            return MarkerlessExpHolder(itemView)
        }

        override fun getItemCount() = mList.size

        override fun onBindViewHolder(holder: MarkerlessExpHolder, position: Int) {
            val marker = mList[position]
            holder.bind(marker)
            holder.itemView.tag = marker
        }


        inner class MarkerlessExpHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var mThumbnail: ImageView? = null
            var mDescription: TextView? = null

            init {
                mThumbnail = itemView.findViewById(R.id.image_thumbnail)
                mDescription = itemView.findViewById(R.id.txt_description)
                itemView.setOnClickListener(this)
            }

            fun bind(marker: MarkerbasedMarker) {
                mDescription!!.text = marker.title
                Glide.with(context!!).load(marker.icon)
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                    .into(mThumbnail!!)
            }

            override fun onClick(v: View?) {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val path = mList[position].targetFile!!.trim { it <= ' ' }
                    try {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(path)
                        activity!!.startActivity(i)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Can't handle", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}
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
import kotlinx.android.synthetic.main.fragment_download_marker.*

class MarkerFragment : AbstractFragment() {

    override val layoutId = R.layout.fragment_download_marker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_item.layoutManager = LinearLayoutManager(activity)

        showLoading()

        BluDataHelper.getMarkerbasedMarkers(object : DataCallback<MarkerbasedMarker> {
            override fun onSuccess(list: List<MarkerbasedMarker>) {
                list_item.adapter = MarkerlessAdapter(list)
                hideLoading()
            }

            override fun onFail(errorMessage: String) {
                showError(errorMessage)
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
            var image: ImageView = itemView.findViewById(R.id.image_thumbnail)
            var description: TextView = itemView.findViewById(R.id.txt_description)

            init { itemView.setOnClickListener(this) }

            fun bind(marker: MarkerbasedMarker) {
                description.text = marker.title
                Glide.with(context!!).load(marker.icon)
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                    .into(image)
            }

            override fun onClick(v: View?) {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val path = mList[position].targetFile!!.trim { it <= ' ' }
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(path)
                    activity!!.startActivity(intent)
                }
            }
        }

    }

}
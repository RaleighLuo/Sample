package com.gkzxhn.sample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import com.gkzxhn.sample.R
import kotlinx.android.synthetic.main.main_item_layout.view.*
/**
 * Created by Raleigh on 18/6/24.
 */
class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val mData= arrayListOf<String>()
    private val mContent= arrayListOf<String>()
    private var onItemClickListener:OnItemClickListener?=null
    fun loadItem(title: String, breif: String){
        mData.add(title)
        mContent.add(breif)
    }
    fun setOnItemClickListener(onItemClickListener:OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.main_item_layout,null,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            tvContent.setText(mData[position])
            tvBreif.setText(mContent[position])
            this.setOnClickListener {
                onItemClickListener?.onClick(it,position)
            }
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {}
}
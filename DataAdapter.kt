package com.example.demoproject.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoproject.Model.DataModel
import com.example.demoproject.R
import com.example.demoproject.databinding.ItemDataBinding

class DataAdapter(
    var context: Context
) : RecyclerView.Adapter<DataAdapter.MyViewHolder>() {
//    var datalist: MutableList<DataModel.Datum>
    var binding: ItemDataBinding? = null
    var TAG = "DataAdapter"
    var cameFrom = ""
    private var datalist: ArrayList<DataModel.Datum>? = null

    init {
        this.cameFrom = cameFrom
        datalist = ArrayList<DataModel.Datum>()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_data, parent, false
        )
        return MyViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val modelClass = datalist!![holder.adapterPosition]
        binding!!.data = modelClass
        if (modelClass.avatar!=null){
            Glide.with(context)
                .load(Uri.parse(modelClass.avatar))
                .into( binding!!.ivAvtar);
        }



    }

    override fun getItemCount(): Int {
        return if (datalist == null) 0 else datalist!!.size

    }

    fun addAll(data: List<DataModel.Datum>) {
        for (data1 in data) {
            add(data1)
        }
    }

    fun add(data1: DataModel.Datum) {
        datalist!!.add(data1)
        notifyItemInserted(datalist!!.size - 1)
    }
    class MyViewHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}

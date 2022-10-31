package com.arya.e_kinerja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.DataItem

class BawahanArrayAdapter(
    context: Context,
    layoutResourceId: Int,
    listBawahan: List<DataItem>
) : ArrayAdapter<DataItem>(context, layoutResourceId, listBawahan) {

    private val arrayListBawahan = ArrayList(listBawahan)

    override fun getCount(): Int {
        return arrayListBawahan.size
    }

    override fun getItem(position: Int): DataItem? {
        return arrayListBawahan[position]
    }

    override fun getItemId(position: Int): Long {
        return arrayListBawahan[position].idPns?.toLong() ?: 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        if (mConvertView == null) {
            mConvertView = LayoutInflater.from(context).inflate(
                R.layout.item_dropdown, parent, false
            )
        }

        val tvDropdownItem = mConvertView!!.findViewById<TextView>(R.id.tv_dropdown_item)

        val bawahan: DataItem? = getItem(position)
        if (bawahan != null) {
            tvDropdownItem.text = bawahan.nama
        }

        return mConvertView
    }
}
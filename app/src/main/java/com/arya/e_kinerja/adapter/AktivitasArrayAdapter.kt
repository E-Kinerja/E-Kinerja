package com.arya.e_kinerja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.Aktivitas

class AktivitasArrayAdapter(
    context: Context,
    layoutResourceId: Int,
    listAktivitas: List<Aktivitas>
) : ArrayAdapter<Aktivitas>(context, layoutResourceId, listAktivitas) {

    private val arrayListAktivitas = ArrayList(listAktivitas)

    override fun getCount(): Int {
        return arrayListAktivitas.size
    }

    override fun getItem(position: Int): Aktivitas? {
        return arrayListAktivitas[position]
    }

    override fun getItemId(position: Int): Long {
        return arrayListAktivitas[position].id?.toLong() ?: 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                R.layout.item_dropdown, parent, false
            )
        }

        val tvDropdownItem = view!!.findViewById<TextView>(R.id.tv_dropdown_item)

        val aktivitas: Aktivitas? = getItem(position)
        if (aktivitas != null) {
            tvDropdownItem.text = aktivitas.bkNamaKegiatan
        }

        return view
    }
}
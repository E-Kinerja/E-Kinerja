package com.arya.e_kinerja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.Aktivitas

class AktivitasArrayAdapter(
    context: Context,
    layoutResourceId: Int,
    listAktivitas: List<Aktivitas>
) : ArrayAdapter<Aktivitas>(context, layoutResourceId, listAktivitas) {

    private val arrayListAktivitas = ArrayList(listAktivitas)
    private val arrayListAllAktivitas = ArrayList(listAktivitas)

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
        var mConvertView = convertView
        if (mConvertView == null) {
            mConvertView = LayoutInflater.from(context).inflate(
                R.layout.item_dropdown, parent, false
            )
        }

        val tvDropdownItem = mConvertView!!.findViewById<TextView>(R.id.tv_dropdown_item)

        val aktivitas: Aktivitas? = getItem(position)
        if (aktivitas != null) {
            tvDropdownItem.text = aktivitas.bkNamaKegiatan
        }

        return mConvertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Aktivitas).bkNamaKegiatan.toString()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val namaKegiatanSuggestion: MutableList<Aktivitas> = ArrayList()
                    for (aktivitas in arrayListAllAktivitas) {
                        if (aktivitas.bkNamaKegiatan?.lowercase()?.contains(constraint.toString().lowercase()) == true) {
                            namaKegiatanSuggestion.add(aktivitas)
                        }
                    }
                    filterResults.values = namaKegiatanSuggestion
                    filterResults.count = namaKegiatanSuggestion.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayListAktivitas.clear()
                if (results != null) {
                    if (results.count > 0) {
                        for (result in results.values as List<*>) {
                            if (result is Aktivitas) {
                                arrayListAktivitas.add(result)
                            }
                        }
                        notifyDataSetChanged()
                    } else if (constraint == null) {
                        arrayListAktivitas.addAll(arrayListAllAktivitas)
                        notifyDataSetInvalidated()
                    }
                }
            }
        }
    }
}
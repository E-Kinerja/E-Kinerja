package com.arya.e_kinerja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.e_kinerja.data.remote.response.Aktivitas
import com.arya.e_kinerja.databinding.ItemAktivitasBinding

class AktivitasAdapter : ListAdapter<Aktivitas, AktivitasAdapter.AktivitasViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Aktivitas) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AktivitasViewHolder {
        return AktivitasViewHolder(
            ItemAktivitasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AktivitasViewHolder, position: Int) {
        val aktivitas = getItem(position)
        holder.bind(aktivitas)
    }

    inner class AktivitasViewHolder(private val binding: ItemAktivitasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(aktivitas: Aktivitas) {
            binding.tvNamaKegiatan.text = aktivitas.bkNamaKegiatan.toString()

            itemView.setOnClickListener {
                onItemClick?.invoke(aktivitas)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Aktivitas>() {
            override fun areItemsTheSame(oldItem: Aktivitas, newItem: Aktivitas): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Aktivitas, newItem: Aktivitas): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
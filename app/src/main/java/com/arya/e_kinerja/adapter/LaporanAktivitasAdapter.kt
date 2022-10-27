package com.arya.e_kinerja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import com.arya.e_kinerja.databinding.ItemLaporanAktivitasBinding
import com.arya.e_kinerja.utils.dateTimeFormat

class LaporanAktivitasAdapter : ListAdapter<TugasAktivitasResponse, LaporanAktivitasAdapter.LaporanAktivitasViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanAktivitasViewHolder {
        return LaporanAktivitasViewHolder(
            ItemLaporanAktivitasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LaporanAktivitasViewHolder, position: Int) {
        val laporanAktivitas = getItem(position)
        holder.bind(laporanAktivitas)
    }

    inner class LaporanAktivitasViewHolder(private val binding: ItemLaporanAktivitasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tugasAktivitasResponse: TugasAktivitasResponse) {
            var isExpanded = false

            binding.tvTanggalWaktu.text = dateTimeFormat(
                tugasAktivitasResponse.tglakt.toString(),
                null,
                null,
                tugasAktivitasResponse.durasi.toString()
            )

            binding.tvAktivitas.text = tugasAktivitasResponse.aktivitas?.bkNamaKegiatan
            binding.tvCatatan.text = tugasAktivitasResponse.detailakt
            binding.tvOutput.text = tugasAktivitasResponse.output

            binding.tvSelengkapnya.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded)  {
                    binding.tvAktivitas.maxLines = Int.MAX_VALUE
                    binding.tvKetCatatan.visibility = View.VISIBLE
                    binding.tvCatatan.visibility = View.VISIBLE
                    binding.tvKetOutput.visibility = View.VISIBLE
                    binding.tvOutput.visibility = View.VISIBLE

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up, 0, 0, 0
                    )
                } else {
                    binding.tvAktivitas.maxLines = 1
                    binding.tvKetCatatan.visibility = View.GONE
                    binding.tvCatatan.visibility = View.GONE
                    binding.tvKetOutput.visibility = View.GONE
                    binding.tvOutput.visibility = View.GONE

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down, 0, 0, 0
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TugasAktivitasResponse>() {
            override fun areItemsTheSame(oldItem: TugasAktivitasResponse, newItem: TugasAktivitasResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TugasAktivitasResponse, newItem: TugasAktivitasResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
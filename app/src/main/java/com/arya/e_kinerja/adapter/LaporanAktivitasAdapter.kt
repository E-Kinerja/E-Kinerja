package com.arya.e_kinerja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.databinding.ItemLaporanAktivitasBinding
import com.arya.e_kinerja.utils.dateTimeFormat
import com.arya.e_kinerja.utils.gone
import com.arya.e_kinerja.utils.visible

class LaporanAktivitasAdapter : ListAdapter<GetTugasAktivitasResponse, LaporanAktivitasAdapter.LaporanAktivitasViewHolder>(DIFF_CALLBACK) {

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
        fun bind(getTugasAktivitasResponse: GetTugasAktivitasResponse) {
            var isExpanded = false

            binding.tvTanggalWaktu.text = dateTimeFormat(
                getTugasAktivitasResponse.tglakt.toString(),
                null,
                null,
                getTugasAktivitasResponse.durasi.toString()
            )

            binding.tvAktivitas.text = getTugasAktivitasResponse.aktivitas?.bkNamaKegiatan
            binding.tvCatatan.text = getTugasAktivitasResponse.detailakt
            binding.tvOutput.text = getTugasAktivitasResponse.output

            binding.tvSelengkapnya.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded)  {
                    binding.tvAktivitas.maxLines = Int.MAX_VALUE
                    binding.tvKetCatatan.visible()
                    binding.tvCatatan.visible()
                    binding.tvKetOutput.visible()
                    binding.tvOutput.visible()

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up_14, 0, 0, 0
                    )
                } else {
                    binding.tvAktivitas.maxLines = 1
                    binding.tvKetCatatan.gone()
                    binding.tvCatatan.gone()
                    binding.tvKetOutput.gone()
                    binding.tvOutput.gone()

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down_14, 0, 0, 0
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetTugasAktivitasResponse>() {
            override fun areItemsTheSame(oldItem: GetTugasAktivitasResponse, newItem: GetTugasAktivitasResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GetTugasAktivitasResponse, newItem: GetTugasAktivitasResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
package com.arya.e_kinerja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import com.arya.e_kinerja.databinding.ItemTugasAktivitasBinding
import com.arya.e_kinerja.utils.dateTimeFormat

class TugasAktivitasAdapter : ListAdapter<TugasAktivitasResponse, TugasAktivitasAdapter.TugasAktivitasViewHolder>(DIFF_CALLBACK) {

    var onBtnEditClick: ((TugasAktivitasResponse) -> Unit)? = null
    var onBtnHapusClick: ((TugasAktivitasResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasAktivitasViewHolder {
        return TugasAktivitasViewHolder(
            ItemTugasAktivitasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TugasAktivitasViewHolder, position: Int) {
        val tugasAktivitas = getItem(position)
        holder.bind(tugasAktivitas)
    }

    inner class TugasAktivitasViewHolder(private val binding: ItemTugasAktivitasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tugasAktivitasResponse: TugasAktivitasResponse) {
            var isExpanded = false

            binding.tvTanggalWaktu.text = dateTimeFormat(
                tugasAktivitasResponse.tglakt.toString(),
                tugasAktivitasResponse.jammulai.toString(),
                tugasAktivitasResponse.jamselesai.toString(),
                tugasAktivitasResponse.durasi.toString()
            )
            binding.tvAktivitas.text = tugasAktivitasResponse.aktivitas?.bkNamaKegiatan
            binding.tvCatatan.text = tugasAktivitasResponse.detailakt
            binding.tvOutput.text = tugasAktivitasResponse.output
            binding.tvStatus.apply {
                if(tugasAktivitasResponse.status == true) {
                    text = resources.getString(R.string.sudah_dinilai)
                    setTextColor(ContextCompat.getColor(context, R.color.cyan))
                } else {
                    text = resources.getString(R.string.belum_dinilai)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }
            }

            binding.tvSelengkapnya.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded)  {
                    binding.tvAktivitas.maxLines = Int.MAX_VALUE
                    binding.tvKetCatatan.visibility = View.VISIBLE
                    binding.tvCatatan.visibility = View.VISIBLE
                    binding.tvKetOutput.visibility = View.VISIBLE
                    binding.tvOutput.visibility = View.VISIBLE

                    if (tugasAktivitasResponse.status.toString() === "true") {
                        binding.btnEdit.visibility = View.GONE
                        binding.btnHapus.visibility = View.GONE
                    } else {
                        binding.btnEdit.visibility = View.VISIBLE
                        binding.btnHapus.visibility = View.VISIBLE
                    }

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up, 0, 0, 0
                    )
                } else {
                    binding.tvAktivitas.maxLines = 1
                    binding.tvKetCatatan.visibility = View.GONE
                    binding.tvCatatan.visibility = View.GONE
                    binding.tvKetOutput.visibility = View.GONE
                    binding.tvOutput.visibility = View.GONE
                    binding.btnEdit.visibility = View.GONE
                    binding.btnHapus.visibility = View.GONE

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down, 0, 0, 0
                    )
                }
            }

            binding.btnEdit.setOnClickListener {
                onBtnEditClick?.invoke(tugasAktivitasResponse)
            }

            binding.btnHapus.setOnClickListener {
                isExpanded = !isExpanded
                onBtnHapusClick?.invoke(tugasAktivitasResponse)
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
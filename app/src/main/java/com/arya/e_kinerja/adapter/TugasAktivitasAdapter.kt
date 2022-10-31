package com.arya.e_kinerja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.databinding.ItemTugasAktivitasBinding
import com.arya.e_kinerja.utils.dateTimeFormat
import com.arya.e_kinerja.utils.gone
import com.arya.e_kinerja.utils.visible

class TugasAktivitasAdapter : ListAdapter<GetTugasAktivitasResponse, TugasAktivitasAdapter.TugasAktivitasViewHolder>(DIFF_CALLBACK) {

    var onBtnEditClick: ((GetTugasAktivitasResponse) -> Unit)? = null
    var onBtnHapusClick: ((GetTugasAktivitasResponse) -> Unit)? = null

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
        fun bind(getTugasAktivitasResponse: GetTugasAktivitasResponse) {
            var isExpanded = false

            binding.tvTanggalWaktu.text = dateTimeFormat(
                getTugasAktivitasResponse.tglakt.toString(),
                getTugasAktivitasResponse.jammulai.toString(),
                getTugasAktivitasResponse.jamselesai.toString(),
                getTugasAktivitasResponse.durasi.toString()
            )
            binding.tvAktivitas.text = getTugasAktivitasResponse.aktivitas?.bkNamaKegiatan
            binding.tvCatatan.text = getTugasAktivitasResponse.detailakt
            binding.tvOutput.text = getTugasAktivitasResponse.output
            binding.tvStatus.apply {
                if(getTugasAktivitasResponse.status == true) {
                    text = resources.getString(R.string.sudah_dinilai)
                    setTextColor(ContextCompat.getColor(context, R.color.teal))
                } else {
                    text = resources.getString(R.string.belum_dinilai)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }
            }

            binding.tvSelengkapnya.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded)  {
                    binding.tvAktivitas.maxLines = Int.MAX_VALUE
                    binding.tvKetCatatan.visible()
                    binding.tvCatatan.visible()
                    binding.tvKetOutput.visible()
                    binding.tvOutput.visible()

                    if (getTugasAktivitasResponse.status == true) {
                        binding.btnEdit.gone()
                        binding.btnHapus.gone()
                    } else {
                        binding.btnEdit.visible()
                        binding.btnHapus.visible()
                    }

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up_14, 0, 0, 0
                    )
                } else {
                    binding.tvAktivitas.maxLines = 1
                    binding.tvKetCatatan.gone()
                    binding.tvCatatan.gone()
                    binding.tvKetOutput.gone()
                    binding.tvOutput.gone()
                    binding.btnEdit.gone()
                    binding.btnHapus.gone()

                    binding.tvSelengkapnya.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down_14, 0, 0, 0
                    )
                }
            }

            binding.btnEdit.setOnClickListener {
                onBtnEditClick?.invoke(getTugasAktivitasResponse)
            }

            binding.btnHapus.setOnClickListener {
                isExpanded = !isExpanded
                onBtnHapusClick?.invoke(getTugasAktivitasResponse)
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
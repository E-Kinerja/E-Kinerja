package com.arya.e_kinerja.ui.laporan_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arya.e_kinerja.R
import com.arya.e_kinerja.databinding.FragmentLaporanAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LaporanAktivitasFragment : Fragment() {

    private var _binding: FragmentLaporanAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LaporanAktivitasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var (currentBulan, currentTahun) = getCurrentDate()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(
            resources.getStringArray(R.array.bulan)[currentBulan - 1].toString()
        )

        observeSession()
    }

    override fun onResume() {
        super.onResume()

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        val adapterBulan = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayBulan)
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayTahun)

        binding.edtBulan.setAdapter(adapterBulan)
        binding.edtTahun.setAdapter(adapterTahun)
    }

    private fun observeSession() {
        viewModel.sessionEntity.observe(viewLifecycleOwner) {
            binding.tvNamaPegawai.text = it.nama
            binding.tvNipPegawai.text = it.nip
            binding.tvJabatanPegawai.text = it.namaJabatan
            binding.tvInstansiPegawai.text = it.unitKerja
        }
    }

    private fun getCurrentDate(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()

        val formatTahun = SimpleDateFormat("yyyy", Locale.getDefault())
        val currentTahun = formatTahun.format(calendar.time).toInt()

        val formatBulan = SimpleDateFormat("MM", Locale.getDefault())
        val currentBulan = formatBulan.format(calendar.time).toInt()

        return Pair(currentBulan, currentTahun)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
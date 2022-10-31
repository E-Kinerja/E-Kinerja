package com.arya.e_kinerja.ui.laporan_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.LaporanAktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentLaporanAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import com.arya.e_kinerja.utils.gone
import com.arya.e_kinerja.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaporanAktivitasFragment : Fragment() {

    private var _binding: FragmentLaporanAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LaporanAktivitasViewModel by viewModels()

    private var currentBulan = dateFormat(null, "MM").toInt()
    private var currentTahun = dateFormat(null, "yyyy").toInt()

    private lateinit var laporanAktivitasAdapter: LaporanAktivitasAdapter

    private var isCardPejabatExpanded = false
    private var isCardPegawaiExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpRecyclerView()
        setUpAction()
    }

    private fun setUpView() {
        observeGetSession()
        observeGetTotalAktivitas()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())
    }

    private fun setUpRecyclerView() {
        laporanAktivitasAdapter = LaporanAktivitasAdapter()
        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = laporanAktivitasAdapter
    }

    private fun setUpAction() {
        binding.tvPejabat.setOnClickListener {
            isCardPejabatExpanded = !isCardPejabatExpanded
            if (isCardPejabatExpanded) {
                binding.detailPejabat.visible()
                binding.tvPejabat.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_up_16, 0
                )
            } else {
                binding.detailPejabat.gone()
                binding.tvPejabat.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_down_16, 0
                )
            }
        }

        binding.tvPegawai.setOnClickListener {
            isCardPegawaiExpanded = !isCardPegawaiExpanded
            if (isCardPegawaiExpanded) {
                binding.detailPegawai.visible()
                binding.tvPegawai.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_up_16, 0
                )
            } else {
                binding.detailPegawai.gone()
                binding.tvPegawai.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_down_16, 0
                )
            }
        }

        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1
            observeGetTugasAktivitas(null, currentBulan, currentTahun)
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position) as Int
            observeGetTugasAktivitas(null, currentBulan, currentTahun)
        }

        binding.fabPrintAktivitas.setOnClickListener {
//            createPDF()
        }
    }

    private fun observeGetSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            binding.tvNamaPegawai.text = session.nama
            binding.tvNipPegawai.text = session.nip
            binding.tvJabatanPegawai.text = session.namaJabatan
            binding.tvInstansiPegawai.text = session.unitKerja
        }
    }

    private fun observeGetTotalAktivitas() {
        viewModel.getTotalAktivitas().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        binding.tvNilaiAktivitas.text =
                            resources.getString(R.string.total_aktivitas_placeholder, result.data.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun observeGetTugasAktivitas(idPns: Int?, bulan: Int, tahun: Int) {
        viewModel.getTugasAktivitas(idPns, bulan, tahun).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        laporanAktivitasAdapter.submitList(result.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

//    private fun createPDF() {}

    override fun onResume() {
        super.onResume()

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        binding.edtBulan.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayBulan)
        )

        binding.edtTahun.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayTahun)
        )

        observeGetTugasAktivitas(
            null,
            arrayBulan.indexOf(binding.edtBulan.text.toString()) + 1,
            binding.edtTahun.text.toString().toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.arya.e_kinerja.ui.penilaian_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.BawahanArrayAdapter
import com.arya.e_kinerja.adapter.TugasAktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentPenilaianAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PenilaianAktivitasFragment : Fragment() {

    private var _binding: FragmentPenilaianAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PenilaianAktivitasViewModel by viewModels()

    private var currentBulan = dateFormat(null, "MM").toInt()
    private var currentTahun = dateFormat(null, "yyyy").toInt()

    private lateinit var bawahanArrayAdapter: BawahanArrayAdapter
    private lateinit var tugasAktivitasAdapter: TugasAktivitasAdapter

    private var idPns = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPenilaianAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpRecyclerView()
        setUpAction()
    }

    private fun setUpView() {
        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())
    }

    private fun setUpRecyclerView() {
        tugasAktivitasAdapter = TugasAktivitasAdapter()
        binding.rvPenilaianAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvPenilaianAktivitas.adapter = tugasAktivitasAdapter
    }

    private fun setUpAction() {
        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1
            observeGetTugasAktivitas(idPns.toString(), currentBulan.toString(), currentTahun.toString())
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position) as Int
            observeGetTugasAktivitas(idPns.toString(), currentBulan.toString(), currentTahun.toString())
        }

        binding.edtBawahan.setOnItemClickListener { _, _, position, _ ->
            val bawahan = bawahanArrayAdapter.getItem(position)

            idPns = bawahan?.idPns ?: 0

            binding.edtBawahan.setText(bawahan?.nama)

            observeGetTugasAktivitas(idPns.toString(), currentBulan.toString(), currentTahun.toString())
        }
    }

    private fun observeGetTugasAktivitas(idPns: String, bulan: String, tahun: String) {
        viewModel.getTugasAktivitas(idPns, bulan, tahun).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        tugasAktivitasAdapter.submitList(result.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getListBawahan().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        bawahanArrayAdapter = BawahanArrayAdapter(
                            requireContext(),
                            R.layout.item_dropdown,
                            result.data.data
                        )
                        binding.edtBawahan.setAdapter(bawahanArrayAdapter)
                    }
                    is Result.Error -> {}
                }
            }
        }

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        val adapterBulan = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayBulan)
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayTahun)

        binding.edtBulan.setAdapter(adapterBulan)
        binding.edtTahun.setAdapter(adapterTahun)

//        observeGetTugasAktivitas(
//            idPns.toString(),
//            (arrayBulan.indexOf(binding.edtBulan.text.toString()) + 1).toString(),
//            binding.edtTahun.text.toString()
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
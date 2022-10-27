package com.arya.e_kinerja.ui.tugas_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.TugasAktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentTugasAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TugasAktivitasFragment : Fragment() {

    private var _binding: FragmentTugasAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TugasAktivitasViewModel by viewModels()

    private var currentBulan = dateFormat(null, "MM").toInt()
    private var currentTahun = dateFormat(null, "yyyy").toInt()

    private lateinit var tugasAktivitasAdapter: TugasAktivitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTugasAktivitasBinding.inflate(inflater, container, false)
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
        tugasAktivitasAdapter.onBtnEditClick = {
            findNavController().navigate(
                TugasAktivitasFragmentDirections
                    .actionTugasAktivitasFragmentToInputAktivitasFragment(it)
            )
        }

        tugasAktivitasAdapter.onBtnHapusClick = {
            observeDeleteTugasAktivitas(it.id.toString())
        }

        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = tugasAktivitasAdapter
    }

    private fun setUpAction() {
        binding.btnInputAktivitas.setOnClickListener {
            findNavController().navigate(
                TugasAktivitasFragmentDirections
                    .actionTugasAktivitasFragmentToInputAktivitasFragment(null)
            )
        }

        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1
            observeGetTugasAktivitas(currentBulan.toString(), currentTahun.toString())
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position) as Int
            observeGetTugasAktivitas(currentBulan.toString(), currentTahun.toString())
        }
    }

    private fun observeDeleteTugasAktivitas(id: String) {
        viewModel.deleteTugasAktivitas(id).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        observeGetTugasAktivitas(currentBulan.toString(), currentTahun.toString())
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun observeGetTugasAktivitas(bulan: String, tahun: String) {
        viewModel.getTugasAktivitas(bulan, tahun).observe(viewLifecycleOwner) { result ->
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

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        val adapterBulan = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayBulan)
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayTahun)

        binding.edtBulan.setAdapter(adapterBulan)
        binding.edtTahun.setAdapter(adapterTahun)

        observeGetTugasAktivitas(
            (arrayBulan.indexOf(binding.edtBulan.text.toString()) + 1).toString(),
            binding.edtTahun.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
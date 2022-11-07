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
import com.arya.e_kinerja.ui.main.MainActivity
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TugasAktivitasFragment : Fragment() {

    private var _binding: FragmentTugasAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TugasAktivitasViewModel by viewModels()

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
        val currentBulan = dateFormat(null, "MM").toInt()
        val currentTahun = dateFormat(null, "yyyy").toInt()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())

        observeTugasAktivitas()
    }

    private fun setUpRecyclerView() {
        tugasAktivitasAdapter = TugasAktivitasAdapter()

        tugasAktivitasAdapter.onBtnEditClick = {
            findNavController().navigate(
                TugasAktivitasFragmentDirections
                    .actionTugasAktivitasFragmentToInputAktivitasFragment(it, "")
            )
        }

        tugasAktivitasAdapter.onBtnHapusClick = {
            observeDeleteTugasAktivitas(it.id as Int)
        }

        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = tugasAktivitasAdapter
    }

    private fun setUpAction() {
        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            viewModel.setBulan(position + 1)
            viewModel.getTugasAktivitas()
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            viewModel.setTahun(adapterView.adapter.getItem(position).toString().toInt())
            viewModel.getTugasAktivitas()
        }

        (activity as MainActivity).onFabClick = {
            findNavController().navigate(
                TugasAktivitasFragmentDirections
                    .actionTugasAktivitasFragmentToInputAktivitasFragment(null, "")
            )
        }
    }

    private fun observeDeleteTugasAktivitas(id: Int) {
        viewModel.deleteTugasAktivitas(id).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        viewModel.getTugasAktivitas()
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun observeTugasAktivitas() {
        viewModel.tugasAktivitas.observe(viewLifecycleOwner) { result ->
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

        binding.edtBulan.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayBulan)
        )

        binding.edtTahun.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayTahun)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
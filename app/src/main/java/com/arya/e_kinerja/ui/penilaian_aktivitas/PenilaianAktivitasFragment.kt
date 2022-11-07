package com.arya.e_kinerja.ui.penilaian_aktivitas

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
import com.arya.e_kinerja.adapter.BawahanArrayAdapter
import com.arya.e_kinerja.adapter.PenilaianAktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.DataItem
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.databinding.FragmentPenilaianAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PenilaianAktivitasFragment : Fragment() {

    private var _binding: FragmentPenilaianAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PenilaianAktivitasViewModel by viewModels()

    private lateinit var bawahanArrayAdapter: BawahanArrayAdapter
    private lateinit var penilaianAktivitasAdapter: PenilaianAktivitasAdapter

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
        observeGetListBawahan()

        val currentBulan = dateFormat(null, "MM").toInt()
        val currentTahun = dateFormat(null, "yyyy").toInt()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())

        observePenilaianAktivitas()
    }

    private fun setUpRecyclerView() {
        penilaianAktivitasAdapter = PenilaianAktivitasAdapter()

        penilaianAktivitasAdapter.onCheckBoxClick = { getTugasAktivitasResponse: GetTugasAktivitasResponse, isChecked: Boolean ->
            observePostVerifAktivitas((getTugasAktivitasResponse.id as Int), isChecked)
        }

        penilaianAktivitasAdapter.onBtnEditClick = {
            viewModel.nip.observe(viewLifecycleOwner) { nip ->
                findNavController().navigate(
                    PenilaianAktivitasFragmentDirections
                        .actionPenilaianAktivitasFragmentToInputAktivitasFragment(it, nip)
                )
            }
        }

        penilaianAktivitasAdapter.onBtnHapusClick = {
            observeDeleteTugasAktivitas(it.id as Int)
        }

        binding.rvPenilaianAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvPenilaianAktivitas.adapter = penilaianAktivitasAdapter
    }

    private fun setUpAction() {
        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            viewModel.setBulan(position + 1)
            viewModel.getLaporanAktivitas()
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            viewModel.setTahun(adapterView.adapter.getItem(position).toString().toInt())
            viewModel.getLaporanAktivitas()
        }

        binding.edtBawahan.setOnItemClickListener { _, _, position, _ ->
            val bawahan = bawahanArrayAdapter.getItem(position) as DataItem

            binding.edtBawahan.setText(bawahan.nama)

            viewModel.setIdPns(bawahan.idPns as Int)
            viewModel.setNip(bawahan.nip as String)
            viewModel.getLaporanAktivitas()
        }
    }

    private fun observeGetListBawahan() {
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
                        bawahanArrayAdapter.notifyDataSetChanged()
                        binding.edtBawahan.setAdapter(bawahanArrayAdapter)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun observeDeleteTugasAktivitas(id: Int) {
        viewModel.deleteTugasAktivitas(id).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        viewModel.getLaporanAktivitas()
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun observePenilaianAktivitas() {
        viewModel.penilaianAktivitas.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        penilaianAktivitasAdapter.submitList(result.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun observePostVerifAktivitas(id: Int, status: Boolean) {
        viewModel.postVerifTugasAktivitas(id, status).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        viewModel.getLaporanAktivitas()
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
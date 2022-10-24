package com.arya.e_kinerja.ui.tugas_aktivitas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.TugasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentTugasAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint
import org.xmlpull.v1.XmlPullParser.TYPES
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TugasAktivitasFragment : Fragment() {

    private var _binding: FragmentTugasAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TugasAktivitasViewModel by viewModels()

    private lateinit var tugasAdapter: TugasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTugasAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tugasAdapter = TugasAdapter()
        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.setHasFixedSize(true)
        binding.rvTugasAktivitas.adapter = tugasAdapter

        binding.btnInputAktivitas.setOnClickListener {
            findNavController().navigate(
                R.id.action_tugasAktivitasFragment_to_cariAktivitasFragment
            )
        }

        viewModel.getTugasAktivitas("10", "2022").observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        tugasAdapter.submitList(result.data)
                    }
                    is Result.Error -> {}
                }
            }
        }

        getCurrentDate()
        setupDropdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDropdown() {
        binding.edtTahun.setAdapter(setupArrayAdapter(R.array.tahun))
        binding.edtBulan.setAdapter(setupArrayAdapter(R.array.bulan))
    }

    private fun setupArrayAdapter(id: Int): ArrayAdapter<String> {
        val array = resources.getStringArray(id)
        return ArrayAdapter(requireContext(), R.layout.item_dropdown, array)
    }

    private fun getCurrentDate() {
        val calendar = Calendar.getInstance()
        val bulan = resources.getStringArray(R.array.bulan)

        val formatTahun = SimpleDateFormat("yyyy", Locale.getDefault())
        val currentTahun = formatTahun.format(calendar.time)

        val formatBulan = SimpleDateFormat("MM", Locale.getDefault())
        val currentBulan = formatBulan.format(calendar.time)

        binding.edtTahun.setText(currentTahun)
        binding.edtBulan.setText(bulan[currentBulan.toInt()-1].toString())
    }
}
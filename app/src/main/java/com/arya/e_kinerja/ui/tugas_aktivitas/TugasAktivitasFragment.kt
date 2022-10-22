package com.arya.e_kinerja.ui.tugas_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.TugasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentTugasAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint

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

//        tugasAdapter.submitList(
//            arrayListOf(
//                TugasAktivitasResponse(
//                    1, "2022/10/21", null, null, null, null, null, null, null, null,
//                    Aktivitas(
//                        1, null, "Surat", null, null, null, null, null,
//                        null, null, null, null, null, null, null
//                    )
//                )
//            )
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
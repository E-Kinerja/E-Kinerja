package com.arya.e_kinerja.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arya.e_kinerja.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { session ->
//            val mainActivity = (activity as MainActivity)
//            mainActivity.setupNavDrawer(session.level.toString())
//            mainActivity.setupNavHeader(session.nip.toString(), session.namaJabatan.toString())

            binding.tvNama.text = session.nama
            binding.tvNamaJabatan.text = session.namaJabatan
            binding.tvUnitKerja.text = session.unitKerja
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
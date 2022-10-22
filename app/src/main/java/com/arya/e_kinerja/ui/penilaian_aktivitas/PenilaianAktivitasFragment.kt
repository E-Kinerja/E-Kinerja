package com.arya.e_kinerja.ui.penilaian_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arya.e_kinerja.databinding.FragmentPenilaianAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PenilaianAktivitasFragment : Fragment() {

    private var _binding: FragmentPenilaianAktivitasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPenilaianAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.arya.e_kinerja.ui.input_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arya.e_kinerja.R
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentInputAktivitasBinding
import com.arya.e_kinerja.utils.openMaterialDatePicker
import com.arya.e_kinerja.utils.openMaterialTimePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputAktivitasFragment : Fragment() {

    private var _binding: FragmentInputAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InputAktivitasViewModel by viewModels()
    private val args: InputAktivitasFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtAktivitas.setText(args.currentAktivitas.bkNamaKegiatan)
        binding.edtSatuan.setText(args.currentAktivitas.bkSatuanOutput)

        binding.edtTanggal.setOnClickListener {
            openMaterialDatePicker(
                childFragmentManager,
                binding.edtTanggal,
                binding.edtTanggal.text.toString()
            )
        }

        binding.edtJamMulai.setOnClickListener {
            openMaterialTimePicker(
                childFragmentManager,
                binding.edtJamMulai,
                binding.edtJamMulai.text.toString()
            )
        }

        binding.edtJamBerakhir.setOnClickListener {
            openMaterialTimePicker(
                childFragmentManager,
                binding.edtJamBerakhir,
                binding.edtJamBerakhir.text.toString()
            )
        }

        binding.btnSimpan.setOnClickListener {
            viewModel.postInputAktivitas(
                binding.edtTanggal.text.toString(),
                args.currentAktivitas.id.toString(),
                binding.edtCatatan.text.toString(),
                binding.edtOutput.text.toString(),
                binding.edtJamMulai.text.toString(),
                binding.edtJamBerakhir.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                if (result != null)
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            findNavController().navigate(
                                R.id.action_inputAktivitasFragment_to_tugasAktivitasFragment
                            )

                            Snackbar.make(
                                binding.root,
                                "Input Aktivitas Berhasil",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is Result.Error -> {}
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
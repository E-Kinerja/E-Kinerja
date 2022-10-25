package com.arya.e_kinerja.ui.input_aktivitas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.CustomArrayAdapter
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

    private lateinit var customArrayAdapter: CustomArrayAdapter

    private var idAktivitas = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                idAktivitas.toString(),
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

        binding.edtAktivitas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length >= 3) {
                        viewModel.postCariAktivitas(p0.toString()).observe(viewLifecycleOwner) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {}
                                    is Result.Success -> {
                                        customArrayAdapter = CustomArrayAdapter(
                                            requireContext(),
                                            R.layout.item_dropdown,
                                            result.data
                                        )
                                        binding.edtAktivitas.setAdapter(customArrayAdapter)
                                    }
                                    is Result.Error -> {}
                                }
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.edtAktivitas.setOnItemClickListener { _, _, position, _ ->
            val aktivitas = customArrayAdapter.getItem(position)

            idAktivitas = aktivitas?.id ?: 0

            binding.edtAktivitas.setText(aktivitas?.bkNamaKegiatan)
            binding.edtSatuan.setText(aktivitas?.bkSatuanOutput)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
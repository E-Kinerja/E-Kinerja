package com.arya.e_kinerja.ui.input_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.AktivitasArrayAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentInputAktivitasBinding
import com.arya.e_kinerja.utils.openMaterialDatePicker
import com.arya.e_kinerja.utils.openMaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputAktivitasFragment : Fragment() {

    private var _binding: FragmentInputAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InputAktivitasViewModel by viewModels()
    private val args: InputAktivitasFragmentArgs by navArgs()

    private lateinit var aktivitasArrayAdapter: AktivitasArrayAdapter

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

        setUpView()
        setUpAction()
    }

    private fun setUpView() {
        if (args.tugasAktivitas?.id != null) {
            binding.edtTanggal.setText(args.tugasAktivitas?.tglakt)
            binding.edtAktivitas.setText(args.tugasAktivitas?.aktivitas?.bkNamaKegiatan)
            binding.edtCatatan.setText(args.tugasAktivitas?.detailakt)
            binding.edtOutput.setText(args.tugasAktivitas?.output?.split(" ")?.get(0))
            binding.edtSatuan.setText(args.tugasAktivitas?.aktivitas?.bkSatuanOutput)
            binding.edtJamMulai.setText(args.tugasAktivitas?.jammulai)
            binding.edtJamBerakhir.setText(args.tugasAktivitas?.jamselesai)
            binding.btnSimpan.text = resources.getString(R.string.update)
        }
    }

    private fun setUpAction() {
        binding.edtTanggal.setOnClickListener {
            openMaterialDatePicker(
                childFragmentManager, binding.edtTanggal, binding.edtTanggal.text.toString()
            )
        }

        binding.edtJamMulai.setOnClickListener {
            openMaterialTimePicker(
                childFragmentManager, binding.edtJamMulai, binding.edtJamMulai.text.toString()
            )
        }

        binding.edtJamBerakhir.setOnClickListener {
            openMaterialTimePicker(
                childFragmentManager, binding.edtJamBerakhir, binding.edtJamBerakhir.text.toString()
            )
        }

        binding.btnSimpan.setOnClickListener {
            val tanggal = binding.edtTanggal.text.toString()
            val catatan = binding.edtCatatan.text.toString()
            val output = "${binding.edtOutput.text.toString()} ${binding.edtSatuan.text.toString()}"
            val jamMulai = binding.edtJamMulai.text.toString()
            val jamBerakhir = binding.edtJamBerakhir.text.toString()

            if (args.tugasAktivitas?.id != null) {
                val idTugasAktivitas = args.tugasAktivitas?.id.toString()
                observePostEditAktivitas(
                    idTugasAktivitas, tanggal, args.tugasAktivitas?.aktivitas?.id.toString(), catatan, output, jamMulai, jamBerakhir
                )
            } else {
                observePostInputAktivitas(
                    tanggal, idAktivitas.toString(), catatan, output, jamMulai, jamBerakhir
                )
            }
        }

        binding.edtAktivitas.addTextChangedListener(onTextChanged = { text: CharSequence?, _, _, _ ->
            if (text != null && text.length >= 3) {
                observePostCariAktivitas(text)
            }
        })

        binding.edtAktivitas.setOnItemClickListener { _, _, position, _ ->
            val aktivitas = aktivitasArrayAdapter.getItem(position)

            idAktivitas = aktivitas?.id ?: 0

            binding.edtAktivitas.setText(aktivitas?.bkNamaKegiatan)
            binding.edtSatuan.setText(aktivitas?.bkSatuanOutput)
        }
    }

    private fun observePostInputAktivitas(
        tanggal: String,
        idAkt: String,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ) {
        viewModel.postInputAktivitas(
            tanggal, idAkt, catatan, output, jamMulai, jamBerakhir
        ).observe(viewLifecycleOwner) { result ->
            if (result != null)
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        findNavController().navigate(
                            R.id.action_inputAktivitasFragment_to_tugasAktivitasFragment
                        )
                    }
                    is Result.Error -> {}
                }
        }
    }

    private fun observePostEditAktivitas(
        id: String,
        tanggal: String,
        idAkt: String,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ) {

        viewModel.postEditTugasAktivitas(
            id, tanggal, idAkt, catatan, output, jamMulai, jamBerakhir
        ).observe(viewLifecycleOwner) { result ->
            if (result != null)
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        findNavController().navigate(
                            R.id.action_inputAktivitasFragment_to_tugasAktivitasFragment
                        )
                    }
                    is Result.Error -> {}
                }
        }
    }

    private fun observePostCariAktivitas(text: CharSequence?) {
        viewModel.postCariAktivitas(text.toString()).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        aktivitasArrayAdapter = AktivitasArrayAdapter(
                            requireContext(),
                            R.layout.item_dropdown,
                            result.data
                        )
                        binding.edtAktivitas.setAdapter(aktivitasArrayAdapter)
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
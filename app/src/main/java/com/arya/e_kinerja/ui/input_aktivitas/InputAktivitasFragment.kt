package com.arya.e_kinerja.ui.input_aktivitas

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.AktivitasArrayAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentInputAktivitasBinding
import com.arya.e_kinerja.ui.main.MainActivity
import com.arya.e_kinerja.utils.createLoadingDialog
import com.arya.e_kinerja.utils.dateFormat
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
    private lateinit var loadingDialog: Dialog

    private var idTugasAktivitas: Int? = null
    private var nip: String? = null
    private var idAktivitas: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        setUpView()
        setUpAction()
    }

    private fun setUpView() {
        nip = args.nip

        if (args.tugasAktivitas != null) {
            (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.edit_aktivitas)

            idTugasAktivitas = args.tugasAktivitas?.id as Int
            idAktivitas = args.tugasAktivitas?.aktivitas?.id

            binding.edtTanggal.setText(args.tugasAktivitas?.tglakt)
            binding.edtAktivitas.setText(args.tugasAktivitas?.aktivitas?.bkNamaKegiatan)
            binding.edtCatatan.setText(args.tugasAktivitas?.detailakt)
            binding.edtOutput.setText(args.tugasAktivitas?.output?.split(" ")?.get(0))
            binding.edtSatuan.setText(args.tugasAktivitas?.aktivitas?.bkSatuanOutput)
            binding.edtJamMulai.setText(dateFormat(args.tugasAktivitas?.jammulai, "HH:mm"))
            binding.edtJamBerakhir.setText(dateFormat(args.tugasAktivitas?.jamselesai, "HH:mm"))
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

            if (args.tugasAktivitas != null) {
                observePostEditAktivitas(
                    (idTugasAktivitas as Int), nip, tanggal, (idAktivitas as Int), catatan, output, jamMulai, jamBerakhir
                )
            } else {
                observePostInputAktivitas(
                    nip, tanggal, (idAktivitas as Int), catatan, output, jamMulai, jamBerakhir
                )
            }
        }

        binding.edtAktivitas.addTextChangedListener(onTextChanged = { text: CharSequence?, _, _, _ ->
            if (text.toString().length >= 3) {
                observePostCariAktivitas(text)
            }
        })

        binding.edtAktivitas.setOnItemClickListener { _, _, position, _ ->
            val aktivitas = aktivitasArrayAdapter.getItem(position)

            idAktivitas = aktivitas?.id

            binding.edtAktivitas.setText(aktivitas?.bkNamaKegiatan)
            binding.edtSatuan.setText(aktivitas?.bkSatuanOutput)
        }
    }

    private fun observePostInputAktivitas(
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ) {
        viewModel.postInputAktivitas(
            nip, tanggal, idAkt, catatan, output, jamMulai, jamBerakhir
        ).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.show()
                    }
                    is Result.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(
                            R.id.action_inputAktivitasFragment_to_tugasAktivitasFragment
                        )
                    }
                    is Result.Error -> {
                        loadingDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun observePostEditAktivitas(
        id: Int,
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ) {
        viewModel.postEditTugasAktivitas(
            id, nip, tanggal, idAkt, catatan, output, jamMulai, jamBerakhir
        ).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.show()
                    }
                    is Result.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(
                            if (nip.isNullOrEmpty()) {
                                R.id.action_inputAktivitasFragment_to_tugasAktivitasFragment
                            } else {
                                R.id.action_inputAktivitasFragment_to_penilaianAktivitasFragment
                            }
                        )
                    }
                    is Result.Error -> {
                        loadingDialog.dismiss()
                    }
                }
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
                            requireContext(), R.layout.item_dropdown, result.data
                        )
                        aktivitasArrayAdapter.notifyDataSetChanged()
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
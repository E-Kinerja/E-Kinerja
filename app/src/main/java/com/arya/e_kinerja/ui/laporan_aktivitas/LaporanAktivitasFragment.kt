package com.arya.e_kinerja.ui.laporan_aktivitas

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.Environment
import android.print.pdf.PrintedPdfDocument
import android.provider.DocumentsContract.Document
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.TugasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentLaporanAktivitasBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LaporanAktivitasFragment : Fragment() {

    private var _binding: FragmentLaporanAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LaporanAktivitasViewModel by viewModels()

    private lateinit var tugasAdapter: TugasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tugasAdapter = TugasAdapter()
        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = tugasAdapter

        var (currentBulan, currentTahun) = getCurrentDate()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(
            resources.getStringArray(R.array.bulan)[currentBulan - 1].toString()
        )

        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1

            getTugasAktivitas(currentBulan, currentTahun)
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position).toString().toInt()

            getTugasAktivitas(currentBulan, currentTahun)
        }

        binding.btnPrintAktivitas.setOnClickListener {
            createPDF()
        }

        observeSession()
    }

    override fun onResume() {
        super.onResume()

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        val adapterBulan = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayBulan)
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayTahun)

        binding.edtBulan.setAdapter(adapterBulan)
        binding.edtTahun.setAdapter(adapterTahun)

        getTugasAktivitas(
            resources.getStringArray(R.array.bulan).indexOf(
                binding.edtBulan.text.toString()
            ) + 1,
            binding.edtTahun.text.toString().toInt()
        )
    }

    private fun observeSession() {
        viewModel.sessionEntity.observe(viewLifecycleOwner) {
            binding.tvNamaPegawai.text = it.nama
            binding.tvNipPegawai.text = it.nip
            binding.tvJabatanPegawai.text = it.namaJabatan
            binding.tvInstansiPegawai.text = it.unitKerja
        }
    }

    private fun getCurrentDate(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()

        val formatTahun = SimpleDateFormat("yyyy", Locale.getDefault())
        val currentTahun = formatTahun.format(calendar.time).toInt()

        val formatBulan = SimpleDateFormat("MM", Locale.getDefault())
        val currentBulan = formatBulan.format(calendar.time).toInt()

        return Pair(currentBulan, currentTahun)
    }

    private fun getTugasAktivitas(bulan: Int, tahun: Int) {
        viewModel.getTugasAktivitas(bulan.toString(), tahun.toString()).observe(viewLifecycleOwner) { result ->
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
    }

    private fun createPDF() {
        Log.i("CreatePDF", "PDF Terbuat")
        var (currentBulan, currentTahun) = getCurrentDate()
        val bulan = resources.getStringArray(R.array.bulan)[currentBulan - 1].toString()

        val calendar = Calendar.getInstance()
        val clockFormat = SimpleDateFormat("HH-mm-ss", Locale.getDefault())
        val clock = clockFormat.format(calendar.time)

        val pdfDocument = PdfDocument()
        val pageInfo = PageInfo.Builder(750, 563, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        pdfDocument.finishPage(page)

        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
            "Laporan_Aktivitas_${bulan}_${currentTahun} ($clock).pdf"
        )
        pdfDocument.writeTo(FileOutputStream(filePath))
        Log.i("CreatePDF", "PDF Tersimpan")

        pdfDocument.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
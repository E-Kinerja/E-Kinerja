package com.arya.e_kinerja.ui.laporan_aktivitas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.e_kinerja.R
import com.arya.e_kinerja.adapter.LaporanAktivitasAdapter
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.databinding.FragmentLaporanAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaporanAktivitasFragment : Fragment() {

    private var _binding: FragmentLaporanAktivitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LaporanAktivitasViewModel by viewModels()

    private var currentBulan = dateFormat(null, "MM").toInt()
    private var currentTahun = dateFormat(null, "yyyy").toInt()

    private lateinit var laporanAktivitasAdapter: LaporanAktivitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanAktivitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpRecyclerView()
        setUpAction()
    }

    private fun setUpView() {
        observeGetSession()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())
    }

    private fun setUpRecyclerView() {
        laporanAktivitasAdapter = LaporanAktivitasAdapter()
        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = laporanAktivitasAdapter
    }

    private fun setUpAction() {
        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1
            observeGetTugasAktivitas(currentBulan.toString(), currentTahun.toString())
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position) as Int
            observeGetTugasAktivitas(currentBulan.toString(), currentTahun.toString())
        }

        binding.btnPrintAktivitas.setOnClickListener {
            createPDF()
        }
    }

    private fun observeGetSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            binding.tvNamaPegawai.text = session.nama
            binding.tvNipPegawai.text = session.nip
            binding.tvJabatanPegawai.text = session.namaJabatan
            binding.tvInstansiPegawai.text = session.unitKerja
        }
    }

    private fun observeGetTugasAktivitas(bulan: String, tahun: String) {
        viewModel.getTugasAktivitas(bulan, tahun).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        laporanAktivitasAdapter.submitList(result.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun createPDF() {
//        Log.i("CreatePDF", "PDF Terbuat")
//        val bulan = resources.getStringArray(R.array.bulan)[currentBulan - 1].toString()
//
//        val calendar = Calendar.getInstance()
//        val clockFormat = SimpleDateFormat("HH-mm-ss", Locale.getDefault())
//        val clock = clockFormat.format(calendar.time)
//
//        val pdfDocument = PdfDocument()
//        val pageInfo = PageInfo.Builder(816, 1054 , 1).create()
//        val page = pdfDocument.startPage(pageInfo)
//
//        val paint = Paint()
//        val canvas = page.canvas
//
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 1F
//        canvas.drawRect(50F, 50F, pageInfo.pageWidth.toFloat() - 50, 80F, paint)
//        canvas.drawRect(50F, 80F, pageInfo.pageWidth.toFloat() - 50, 110F, paint)
//        canvas.drawRect(50F, 110F, pageInfo.pageWidth.toFloat() - 50, 140F, paint)
//        canvas.drawRect(50F, 140F, pageInfo.pageWidth.toFloat() / 2, 170F, paint)
//        canvas.drawRect(50F, 170F, pageInfo.pageWidth.toFloat() / 2, 200F, paint)
//        canvas.drawRect(pageInfo.pageWidth.toFloat() / 2, 140F, pageInfo.pageWidth.toFloat() - 50, 200F, paint)
//
//        canvas.drawLine(150F, 80F, 150F, 200F, paint)
//        canvas.drawLine(pageInfo.pageWidth.toFloat() / 2, 50F, pageInfo.pageWidth.toFloat() / 2, 200F, paint)
//        canvas.drawLine((pageInfo.pageWidth.toFloat() / 2) + 110, 80F, (pageInfo.pageWidth.toFloat() / 2) + 110, 200F, paint)
//
//        paint.setColor(Color.BLACK)
//        paint.textSize = 14F
//        paint.textAlign = Paint.Align.CENTER
//        paint.style = Paint.Style.FILL
//        canvas.drawText("PEGAWAI YANG DINILAI", 234F, 70F, paint)
//        canvas.drawText("PEJABAT PENILAI KINERJA", 582F, 70F, paint)
//
//        //Pegawai yang dinilai
//        paint.textAlign = Paint.Align.LEFT
//        canvas.drawText("Nama", 60F, 100F, paint)
//        canvas.drawText("NIP", 60F, 130F, paint)
//        canvas.drawText("Jabatan", 60F, 160F, paint)
//        canvas.drawText("Instansi", 60F, 190F, paint)
//
//        viewModel.sessionEntity.observe(viewLifecycleOwner) {
//            canvas.drawText(it.nama.toString(), 160F, 100F, paint)
//            canvas.drawText(it.nip.toString(), 160F, 130F, paint)
//            canvas.drawText(it.namaJabatan.toString(), 160F, 160F, paint)
//            canvas.drawText(it.unitKerja.toString(), 160F, 190F, paint)
//        }
//
//        canvas.drawText("Nama", 418F, 100F, paint)
//        canvas.drawText("NIP", 418F, 130F, paint)
//        canvas.drawText("Jabatan", 418F, 175F, paint)
//
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 1F
//        canvas.drawRect(50F, 225F, pageInfo.pageWidth.toFloat() - 50, 275F, paint)
//
//        canvas.drawLine(80F, 225F, 80F, 275F, paint)
//        canvas.drawLine(200F, 225F, 200F, 275F, paint)
//        canvas.drawLine(pageInfo.pageWidth.toFloat() - 210, 225F, pageInfo.pageWidth.toFloat() - 210, 275F, paint)
//        canvas.drawLine(pageInfo.pageWidth.toFloat() - 110, 225F, pageInfo.pageWidth.toFloat() - 110, 275F, paint)
//
//        paint.setColor(Color.BLACK)
//        paint.textSize = 14F
//        paint.textAlign = Paint.Align.CENTER
//        paint.isFakeBoldText = true
//        paint.style = Paint.Style.FILL
//        canvas.drawText("No", 65F, 255F, paint)
//        canvas.drawText("Tanggal", 140F, 255F, paint)
//        canvas.drawText("Aktivitas", 403F, 255F, paint)
//        canvas.drawText("Output", 656F, 255F, paint)
//        canvas.drawText("Durasi", 736F, 247.5F, paint)
//        canvas.drawText("(Menit)", 736F, 262.5F, paint)

//        val pdfTable = PdfPTable(4)
//        pdfTable.setWidthPercentage(100F)
//        pdfTable.setWidths(floatArrayOf(1F , 2F, 1F, 2F))
//        pdfTable.headerRows  = 1
//        pdfTable.defaultCell.verticalAlignment = Element.ALIGN_CENTER
//        pdfTable.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
//
//        val cell = PdfPCell(Phrase("PEGAWAI YANG DINILAI"))
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        cell.verticalAlignment = Element.ALIGN_MIDDLE
//        cell.setPadding(4F)
//        pdfTable.addCell(cell)

//        pdfDocument.finishPage(page)
//
//        val filePath = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
//            "Laporan_Aktivitas_${bulan}_${currentTahun} ($clock).pdf"
//        )
//        pdfDocument.writeTo(FileOutputStream(filePath))
//        Log.i("CreatePDF", "PDF Tersimpan")
//        Snackbar.make(binding.root, "PDF Tersimpan", Snackbar.LENGTH_SHORT).show()
//
//        pdfDocument.close()
    }

    override fun onResume() {
        super.onResume()

        val arrayBulan = resources.getStringArray(R.array.bulan)
        val arrayTahun = resources.getStringArray(R.array.tahun)

        val adapterBulan = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayBulan)
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayTahun)

        binding.edtBulan.setAdapter(adapterBulan)
        binding.edtTahun.setAdapter(adapterTahun)

        observeGetTugasAktivitas(
            (arrayBulan.indexOf(binding.edtBulan.text.toString()) + 1).toString(),
            binding.edtTahun.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
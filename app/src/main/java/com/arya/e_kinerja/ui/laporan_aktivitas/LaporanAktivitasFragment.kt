package com.arya.e_kinerja.ui.laporan_aktivitas

import android.os.Bundle
import android.os.Environment
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
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.databinding.FragmentLaporanAktivitasBinding
import com.arya.e_kinerja.utils.dateFormat
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
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

    private var currentBulan = dateFormat(null, "MM").toInt()
    private var currentTahun = dateFormat(null, "yyyy").toInt()

    private lateinit var cell: PdfPCell

    private lateinit var laporanAktivitasAdapter: LaporanAktivitasAdapter
    private lateinit var sessionEntity: SessionEntity

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
        observeGetTotalAktivitas()
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
            sessionEntity = session
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
        val bulan = resources.getStringArray(R.array.bulan)[currentBulan - 1].toString()

        val calendar = Calendar.getInstance()
        val formatWaktu = SimpleDateFormat("HH-mm-ss", Locale.getDefault())
        val waktu = formatWaktu.format(calendar.time)

        val formatTanggal = SimpleDateFormat("d", Locale.getDefault())
        val tanggal = formatTanggal.format(calendar.time)

        // Create PDF using iText Library
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(filePath, "Laporan_Aktivitas_${bulan}_${currentTahun}.pdf")
        val outputStream = FileOutputStream(file)

        val paperSize = Rectangle(612F, 792F)
        val document = Document(paperSize)
        PdfWriter.getInstance(document, outputStream)
        document.open()

        val normalFont = Font(Font.FontFamily.UNDEFINED, 9F, Font.NORMAL, BaseColor.BLACK)
        val boldFont = Font(Font.FontFamily.UNDEFINED, 9F, Font.BOLD, BaseColor.BLACK)
        val normalTnrFont = Font(Font.FontFamily.TIMES_ROMAN, 12F, Font.NORMAL, BaseColor.BLACK)
        val boldTnrFont = Font(Font.FontFamily.TIMES_ROMAN, 12F, Font.BOLD, BaseColor.BLACK)

        val alignCenter = Element.ALIGN_CENTER
        val alignLeft = Element.ALIGN_LEFT

        val table1 = PdfPTable(4)
        table1.widthPercentage = 100F
        table1.setWidths(floatArrayOf(1F, 3F, 1F, 3F))

        table1.addCell(createCell("PEGAWAI YANG DINILAI", normalFont, alignCenter, null, 5F, 2, null))
        table1.addCell(createCell("PEJABAT YANG MENILAI", normalFont, alignCenter, null, 5F, 2, null))

        table1.addCell(createCell("Nama", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(sessionEntity.nama.toString(), normalFont, alignLeft, null, 5F, null, null))

        table1.addCell(createCell("Nama", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell("", normalFont, alignLeft, null, 5F, null, null))

        table1.addCell(createCell("NIP", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(sessionEntity.nip.toString(), normalFont, alignLeft, null, 5F, null, null))

        table1.addCell(createCell("NIP", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell("", normalFont, alignLeft, null, 5F, null, null))

        table1.addCell(createCell("Jabatan", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(sessionEntity.namaJabatan.toString(), normalFont, alignLeft, null, 5F, null, null))

        table1.addCell(createCell("Jabatan", normalFont, alignLeft, null, 5F, null, 2))
        table1.addCell(createCell("", normalFont, alignLeft, null, 5F, null, 2))

        table1.addCell(createCell("Institusi", normalFont, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(sessionEntity.unitKerja.toString(), normalFont, alignLeft, null,5F, null, null))

        document.add(table1)

        val table2 = PdfPTable(5)
        table2.widthPercentage = 100F
        table2.setWidths(floatArrayOf(1F, 2F, 6F, 2F, 1F))
        table2.spacingBefore = 15F

        table2.addCell(createCell("No", boldFont, alignCenter, null,5F, null, null))
        table2.addCell(createCell("Tanggal", boldFont, alignCenter, null, 5F, null, null))
        table2.addCell(createCell("Aktivitas", boldFont, alignCenter, null, 5F, null, null))
        table2.addCell(createCell("Output", boldFont, alignCenter, null, 5F, null, null))
        table2.addCell(createCell("Durasi (Menit)", boldFont, alignCenter, null, 5F, null, null))

        for (i in 1..laporanAktivitasAdapter.itemCount){
            table2.addCell(createCell("$i", normalFont, alignCenter, null, 5F, null, null))
            table2.addCell(createCell(laporanAktivitasAdapter.currentList[i - 1].tglakt.toString(), normalFont, alignLeft, null, 5F, null, null))
            table2.addCell(createCell(laporanAktivitasAdapter.currentList[i - 1].aktivitas?.bkNamaKegiatan.toString(), normalFont, alignLeft, null, 5F, null, null))
            table2.addCell(createCell(laporanAktivitasAdapter.currentList[i - 1].output.toString(), normalFont, alignLeft, null, 5F, null, null))
            table2.addCell(createCell(laporanAktivitasAdapter.currentList[i - 1].durasi.toString(), normalFont, alignCenter, null, 5F, null, null))
        }

        table2.addCell(createCell("Nilai Capaian Aktivitas %", normalFont, alignCenter, null, 5F, 4, null))
        table2.addCell(createCell(binding.tvPersentase.text.toString(), normalFont, alignCenter, null, 5F, null, null))

        document.add(table2)

        val table3 = PdfPTable(2)
        table3.widthPercentage = 100F
        table3.setWidths(floatArrayOf(1F, 1F))
        table3.spacingBefore = 50F

        val borderColor = BaseColor(255, 255, 255)

        table3.addCell(createCell("", normalTnrFont, alignCenter, borderColor, 5F, null, null))
        table3.addCell(createCell("Sidoarjo, $tanggal ${resources.getStringArray(R.array.bulan)[currentBulan - 1]} $currentTahun", normalTnrFont, alignCenter, borderColor, 5F, null, null))

        table3.addCell(createCell("Pihak Kedua,", normalTnrFont, alignCenter, borderColor, null, null, null))
        table3.addCell(createCell("Pihak Pertama,", normalTnrFont, alignCenter, borderColor, null, null, null))

        table3.addCell(createCell("", normalTnrFont, alignCenter, borderColor, null, null, null))
        table3.addCell(createCell(sessionEntity.namaJabatan.toString(), normalTnrFont, alignCenter, borderColor, null, null, null))

        table3.addCell(createCell("\nDitandatangani secara elektronik oleh:\n\n\n\n\n", normalTnrFont, alignLeft, borderColor, null, null, 3))
        table3.addCell(createCell("\nDitandatangani secara elektronik oleh:\n\n${sessionEntity.nip}\n${sessionEntity.nama}\n", normalTnrFont, alignLeft, borderColor, null, null, 3))

        val nama1 = Chunk("", boldTnrFont)
        nama1.setUnderline(1F, -1F)
        cell = PdfPCell(Phrase(nama1))
        cell.horizontalAlignment = alignCenter
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.borderColor = borderColor
        table3.addCell(cell)

        val nama2 = Chunk(sessionEntity.nama.toString(), boldTnrFont)
        nama2.setUnderline(1F, -1F)
        cell = PdfPCell(Phrase(nama2))
        cell.horizontalAlignment = alignCenter
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.borderColor = borderColor
        table3.addCell(cell)

        table3.addCell(createCell("", normalTnrFont, alignCenter, borderColor, null, null, null))
        table3.addCell(createCell(sessionEntity.nip.toString(), normalTnrFont, alignCenter, borderColor, null, null, null))

        document.add(table3)

        document.close()
        Snackbar.make(binding.root, "PDF Berhasil Tersimpan", Snackbar.LENGTH_SHORT).show()
    }


    private fun createCell(text: String, font: Font, hAlign: Int, border: BaseColor?, padding: Float?, colspan: Int?, rowspan: Int?): PdfPCell {
        val cell = PdfPCell(Phrase(text, font))
        cell.horizontalAlignment = hAlign
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.borderColor = border

        if (padding != null){
            cell.setPadding(padding)
        }

        if (colspan != null){
            cell.colspan = colspan
        }

        if (rowspan != null){
            cell.rowspan = rowspan
        }

        return cell
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

    private fun observeGetTotalAktivitas() {
        viewModel.getTotalAktivitas().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        binding.tvPersentase.text =
                            resources.getString(R.string.total_aktivitas, result.data.data)
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
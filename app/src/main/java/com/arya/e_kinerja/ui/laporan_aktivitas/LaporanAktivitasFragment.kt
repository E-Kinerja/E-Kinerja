package com.arya.e_kinerja.ui.laporan_aktivitas

import android.graphics.Bitmap
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
import com.arya.e_kinerja.utils.*
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BarcodeQRCode
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

    private lateinit var laporanAktivitasAdapter: LaporanAktivitasAdapter
    private lateinit var sessionEntity: SessionEntity

    private var isCardPejabatExpanded = false
    private var isCardPegawaiExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        observeGetTotalAktivitas()

        binding.edtTahun.setText(currentTahun.toString())
        binding.edtBulan.setText(resources.getStringArray(R.array.bulan)[currentBulan - 1].toString())

        binding.tvCapaianAktivitas.text =
            resources.getString(R.string.capaian_aktivitas, getNameOfTheMonth(requireContext(), null))
    }

    private fun setUpRecyclerView() {
        laporanAktivitasAdapter = LaporanAktivitasAdapter()
        binding.rvTugasAktivitas.layoutManager = LinearLayoutManager(context)
        binding.rvTugasAktivitas.adapter = laporanAktivitasAdapter
    }

    private fun setUpAction() {
        binding.tvPejabat.setOnClickListener {
            isCardPejabatExpanded = !isCardPejabatExpanded
            if (isCardPejabatExpanded) {
                binding.detailPejabat.visible()
                binding.tvPejabat.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_up_16, 0
                )
            } else {
                binding.detailPejabat.gone()
                binding.tvPejabat.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_down_16, 0
                )
            }
        }

        binding.tvPegawai.setOnClickListener {
            isCardPegawaiExpanded = !isCardPegawaiExpanded
            if (isCardPegawaiExpanded) {
                binding.detailPegawai.visible()
                binding.tvPegawai.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_up_16, 0
                )
            } else {
                binding.detailPegawai.gone()
                binding.tvPegawai.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_down_16, 0
                )
            }
        }

        binding.edtBulan.setOnItemClickListener { _, _, position, _ ->
            currentBulan = position + 1
            observeGetTugasAktivitas(null, currentBulan, currentTahun)

            binding.tvCapaianAktivitas.text =
                resources.getString(R.string.capaian_aktivitas, getNameOfTheMonth(requireContext(), position))
        }

        binding.edtTahun.setOnItemClickListener { adapterView, _, position, _ ->
            currentTahun = adapterView.adapter.getItem(position) as Int
            observeGetTugasAktivitas(null, currentBulan, currentTahun)
        }

        binding.fabPrintAktivitas.setOnClickListener {
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

    private fun observeGetTotalAktivitas() {
        viewModel.getTotalAktivitas().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        binding.tvNilaiAktivitas.text =
                            resources.getString(R.string.total_aktivitas_placeholder, result.data.data)
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun observeGetTugasAktivitas(idPns: Int?, bulan: Int, tahun: Int) {
        viewModel.getTugasAktivitas(idPns, bulan, tahun).observe(viewLifecycleOwner) { result ->
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

        table1.addCell(createCell(Phrase("PEGAWAI YANG DINILAI", normalFont), null, alignCenter, null, 5F, 2, null))
        table1.addCell(createCell(Phrase("PEJABAT YANG MENILAI", normalFont), null, alignCenter, null, 5F, 2, null))

        table1.addCell(createCell(Phrase("Nama", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase(sessionEntity.nama.toString(), normalFont), null, alignLeft, null, 5F, null, null))

        table1.addCell(createCell(Phrase("Nama", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase("Nama Atasan", normalFont), null, alignLeft, null, 5F, null, null))

        table1.addCell(createCell(Phrase("NIP", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase(sessionEntity.nip.toString(), normalFont), null, alignLeft, null, 5F, null, null))

        table1.addCell(createCell(Phrase("NIP", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase("NIP Atasan", normalFont), null, alignLeft, null, 5F, null, null))

        table1.addCell(createCell(Phrase("Jabatan", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase(sessionEntity.nama.toString(), normalFont), null, alignLeft, null, 5F, null, null))

        table1.addCell(createCell(Phrase("Jabatan", normalFont), null, alignLeft, null, 5F, null, 2))
        table1.addCell(createCell(Phrase("Jabatan Atasan", normalFont), null, alignLeft, null, 5F, null, 2))

        table1.addCell(createCell(Phrase("Insitusi", normalFont), null, alignLeft, null, 5F, null, null))
        table1.addCell(createCell(Phrase(sessionEntity.unitKerja.toString(), normalFont), null, alignLeft, null, 5F, null, null))

        document.add(table1)

        val table2 = PdfPTable(5)
        table2.widthPercentage = 100F
        table2.setWidths(floatArrayOf(1F, 2F, 6F, 2F, 1F))
        table2.spacingBefore = 15F

        val keterangan = arrayOf("No", "Tanggal", "Aktivitas", "Output", "Durasi (Menit)")
        for (i in 0..4) {
            table2.addCell(createCell(Phrase(keterangan[i], boldFont), null, alignCenter, null,5F, null, null))
        }

        for (i in 1..laporanAktivitasAdapter.itemCount){
            val tglAkt = laporanAktivitasAdapter.currentList[i - 1].tglakt.toString()
            val aktivitas = laporanAktivitasAdapter.currentList[i - 1].aktivitas?.bkNamaKegiatan.toString()
            val output = laporanAktivitasAdapter.currentList[i - 1].output.toString()
            val durasi = laporanAktivitasAdapter.currentList[i - 1].durasi.toString()

            table2.addCell(createCell(Phrase("$i", normalFont), null, alignCenter, null, 5F, null, null))
            table2.addCell(createCell(Phrase(tglAkt, normalFont), null, alignCenter, null, 5F, null, null))
            table2.addCell(createCell(Phrase(aktivitas, normalFont), null, alignCenter, null, 5F, null, null))
            table2.addCell(createCell(Phrase(output, normalFont), null, alignCenter, null, 5F, null, null))
            table2.addCell(createCell(Phrase(durasi, normalFont), null, alignCenter, null, 5F, null, null))
        }

        table2.addCell(createCell(Phrase("Nilai Capaian Aktivitas %", normalFont), null, alignCenter, null, 5F, 4, null))
        table2.addCell(createCell(Phrase(binding.tvNilaiAktivitas.text.toString(), normalFont), null, alignCenter, null, 5F, null, null))

        document.add(table2)

        val table3 = PdfPTable(4)
        table3.widthPercentage = 100F
        table3.setWidths(floatArrayOf(1F, 3.5F, 1F, 3.5F))
        table3.spacingBefore = 50F

        val borderColor = BaseColor(255, 255, 255)

        table3.addCell(createCell(Phrase("", normalTnrFont), null, alignCenter, borderColor, 5F, 2, null))
        table3.addCell(createCell(Phrase("Sidoarjo, $tanggal ${resources.getStringArray(R.array.bulan)[currentBulan - 1]} $currentTahun", normalTnrFont), null, alignCenter, borderColor, 5F, 2, null))

        table3.addCell(createCell(Phrase("Pihak Kedua,", normalTnrFont), null, alignCenter, borderColor, null, 2, null))
        table3.addCell(createCell(Phrase("Pihak Pertama,", normalTnrFont), null, alignCenter, borderColor, null, 2, null))

        table3.addCell(createCell(Phrase("Jabatan Atasan", normalTnrFont), null, alignCenter, borderColor, null, 2, null))
        table3.addCell(createCell(Phrase(sessionEntity.namaJabatan.toString(), normalTnrFont), null, alignCenter, borderColor, null, 2, null))

        val qrCode1 = BarcodeQRCode("NIP Atasan", 70, 70, null)
        val qrImage1 = qrCode1.image
        table3.addCell(createCell(null, qrImage1, alignCenter, borderColor, null, null, 3))
        table3.addCell(createCell(Phrase("Ditandatangani secara elektronik oleh:\n\nNIP Atasan\nNama Atasan\n", normalTnrFont), null, alignLeft, borderColor, null, null, 3))

        val qrCode2 = BarcodeQRCode(sessionEntity.nip.toString(), 70, 70, null)
        val qrImage2 = qrCode2.image
        table3.addCell(createCell(null, qrImage2, alignCenter, borderColor, null, null, 3))
        table3.addCell(createCell(Phrase("Ditandatangani secara elektronik oleh:\n\n${sessionEntity.nip}\n${sessionEntity.nama}\n", normalTnrFont), null, alignLeft, borderColor, null, null, 3))

        val nama1 = Chunk("Nama Atasan", boldTnrFont)
        nama1.setUnderline(1F, -1F)
        table3.addCell(createCell(Phrase(nama1), null, alignCenter, borderColor, null, 2, null))

        val nama2 = Chunk(sessionEntity.nama.toString(), boldTnrFont)
        nama2.setUnderline(1F, -1F)
        table3.addCell(createCell(Phrase(nama2), null, alignCenter, borderColor, null, 2, null))

        table3.addCell(createCell(Phrase("NIP Atasan", normalTnrFont), null, alignCenter, borderColor, null, 2, null))
        table3.addCell(createCell(Phrase(sessionEntity.nip.toString(), normalTnrFont), null, alignCenter, borderColor, null, 2, null))

        document.add(table3)

        document.close()
        binding.root.showSnackBar("PDF Berhasil Tersimpan", Snackbar.LENGTH_SHORT, null, binding.fabPrintAktivitas) {}
    }

    private fun createCell(phrase: Phrase?, image: Image?, hAlign: Int, border: BaseColor?, padding: Float?, colspan: Int?, rowspan: Int?): PdfPCell {

        val cell = if (phrase != null) PdfPCell(phrase) else PdfPCell(image)
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

        binding.edtBulan.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayBulan)
        )

        binding.edtTahun.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tv_dropdown_item, arrayTahun)
        )

        observeGetTugasAktivitas(
            null,
            arrayBulan.indexOf(binding.edtBulan.text.toString()) + 1,
            binding.edtTahun.text.toString().toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
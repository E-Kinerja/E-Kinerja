package com.arya.e_kinerja.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class TugasAktivitasResponse(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tglakt")
	val tglakt: String? = null,

	@field:SerializedName("detailakt")
	val detailakt: String? = null,

	@field:SerializedName("output")
	val output: String? = null,

	@field:SerializedName("jammulai")
	val jammulai: String? = null,

	@field:SerializedName("jamselesai")
	val jamselesai: String? = null,

	@field:SerializedName("durasi")
	val durasi: Int? = null,

	@field:SerializedName("id_akt")
	val idAkt: Int? = null,

	@field:SerializedName("id_nip")
	val idNip: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("aktivitas")
	val aktivitas: @RawValue Aktivitas? = null
) : Parcelable

data class Aktivitas(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("bk_id")
	val bkId: Int? = null,

	@field:SerializedName("bk_nama_kegiatan")
	val bkNamaKegiatan: String? = null,

	@field:SerializedName("bk_satuan_output")
	val bkSatuanOutput: String? = null,

	@field:SerializedName("bk_kelompok")
	val bkKelompok: String? = null,

	@field:SerializedName("bk_waktu")
	val bkWaktu: String? = null,

	@field:SerializedName("bk_nilai_kegiatan")
	val bkNilaiKegiatan: String? = null,

	@field:SerializedName("bk_kesulitan")
	val bkKesulitan: String? = null,

	@field:SerializedName("bk_jnsjab")
	val bkJnsjab: String? = null,

	@field:SerializedName("bk_nmjab")
	val bkNmjab: String? = null,

	@field:SerializedName("bk_ak")
	val bkAk: String? = null,

	@field:SerializedName("bk_pelaksanakeg")
	val bkPelaksanakeg: String? = null,

	@field:SerializedName("bk_kota")
	val bkKota: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null
)

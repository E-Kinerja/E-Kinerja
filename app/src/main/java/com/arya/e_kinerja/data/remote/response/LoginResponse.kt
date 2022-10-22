package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("access_token")
    val accessToken: String? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("token_type")
    val tokenType: String? = null,
)

data class Data(
    @field:SerializedName("keterangan")
    val keterangan: String? = null,

    @field:SerializedName("status_jabatan")
    val statusJabatan: Int? = null,

    @field:SerializedName("unit_kerja")
    val unitKerja: String? = null,

    @field:SerializedName("nuptk")
    val nuptk: String? = null,

    @field:SerializedName("level")
    val level: String? = null,

    @field:SerializedName("kode_jabatan")
    val kodeJabatan: String? = null,

    @field:SerializedName("eselon")
    val eselon: String? = null,

    @field:SerializedName("jenisjab")
    val jenisjab: String? = null,

    @field:SerializedName("id_pns")
    val idPns: Int? = null,

    @field:SerializedName("id_jabatan_detail")
    val idJabatanDetail: Int? = null,

    @field:SerializedName("nip")
    val nip: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("tempat_lahir")
    val tempatLahir: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("golongan")
    val golongan: String? = null,

    @field:SerializedName("image_profile")
    val imageProfile: String? = null,

    @field:SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null,

    @field:SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,

    @field:SerializedName("nama_jabatan")
    val namaJabatan: String? = null,
)
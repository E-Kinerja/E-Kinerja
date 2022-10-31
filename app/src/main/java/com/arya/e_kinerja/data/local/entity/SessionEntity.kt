package com.arya.e_kinerja.data.local.entity

class SessionEntity(
    val token: String? = null,
    val idPns: Int? = null,
    val level: String? = null,
    val nip: String? = null,
    val nama: String? = null,
    val kodeJabatan: String? = null,
    val namaJabatan: String? = null,
    val unitKerja: String? = null,
    val state: Boolean? = false
)
package com.arya.e_kinerja.data.remote.retrofit

import com.arya.e_kinerja.data.remote.response.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/api/login")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/api/find-aktivitas")
    suspend fun postCariAktivitas(
        @Header("Authorization") token: String,
        @Field("term") term: String
    ): List<Aktivitas>

    @FormUrlEncoded
    @POST("/api/aktivitas")
    suspend fun postInputAktivitas(
        @Field("nip") nip: String,
        @Field("tanggal") tanggal: String,
        @Field("id_akt") idAkt: String,
        @Field("catatan") catatan: String,
        @Field("output") output: String,
        @Field("jam_mulai") jamMulai: String,
        @Field("jam_berakhir") jamBerakhir: String
    ): InputAktivitasResponse

    @GET("/api/list-aktivitas")
    suspend fun getTugasAktivitas(
        @Header("Authorization") token: String,
        @Query("id_pns") idPns: String,
        @Query("bulan") bulan: String,
        @Query("tahun") tahun: String
    ): List<TugasAktivitasResponse>
}
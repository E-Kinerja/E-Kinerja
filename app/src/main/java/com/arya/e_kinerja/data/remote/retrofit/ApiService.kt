package com.arya.e_kinerja.data.remote.retrofit

import com.arya.e_kinerja.data.remote.response.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/api/login")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): PostLoginResponse

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
        @Field("id_akt") idAkt: Int,
        @Field("catatan") catatan: String,
        @Field("output") output: String,
        @Field("jam_mulai") jamMulai: String,
        @Field("jam_berakhir") jamBerakhir: String
    ): PostInputAktivitasResponse

    @GET("/api/list-aktivitas")
    suspend fun getTugasAktivitas(
        @Header("Authorization") token: String,
        @Query("id_pns") idPns: Int,
        @Query("bulan") bulan: Int,
        @Query("tahun") tahun: Int
    ): List<GetTugasAktivitasResponse>

    @DELETE("/api/del-aktivitas/{id}")
    suspend fun deleteTugasAktivitas(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DeleteTugasAktivitasResponse

    @FormUrlEncoded
    @POST("/api/edit-aktivitas")
    suspend fun postEditAktivitas(
        @Header("Authorization") token: String,
        @Field("id") id: Int,
        @Field("nip") nip: String,
        @Field("tanggal") tanggal: String,
        @Field("id_akt") idAkt: Int,
        @Field("catatan") catatan: String,
        @Field("output") output: String,
        @Field("jam_mulai") jamMulai: String,
        @Field("jam_berakhir") jamBerakhir: String
    ): PostEditAktivitasResponse

    @GET("/api/total-aktivitas")
    suspend fun getTotalAktivitas(
        @Header("Authorization") token: String,
        @Query("id_pns") idPns: Int
    ): GetTotalAktivitasResponse

    @GET("/api/aktivitas-bawahan")
    suspend fun getListBawahan(
        @Header("Authorization") token: String,
        @Query("nip") nip: String,
        @Query("kode_jabatan") kodeJabatan: String
    ): GetListBawahanResponse

    @FormUrlEncoded
    @POST("/api/verif-aktivitas")
    suspend fun postVerifTugasAktivitas(
        @Header("Authorization") token: String,
        @Field("idaktivitas") id: Int,
        @Field("status") status: Boolean,
        @Field("bawahan") idPns: Int,
        @Field("bulan") bulan: Int,
        @Field("tahun") tahun: Int
    ): PostVerifAktivitasResponse
}
package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetListBawahanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem>
)

data class DataItem(

	@field:SerializedName("id_pns")
	val idPns: Int? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null
)

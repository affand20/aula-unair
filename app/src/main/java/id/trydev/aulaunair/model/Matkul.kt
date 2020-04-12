package id.trydev.aulaunair.model

import id.trydev.aulaunair.ui.jadwal.model.Mahasiswa

data class Matkul (
    var matkulId:String? = null,
    val dosenId: String? = null,
    val judul: String? = null,
    val jadwal: List<HashMap<String, String>>? = null,
    val mahasiswa: MutableList<Mahasiswa> = mutableListOf()
)
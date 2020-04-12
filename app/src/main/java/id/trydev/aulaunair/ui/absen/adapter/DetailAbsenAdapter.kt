package id.trydev.aulaunair.ui.absen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Materi
import id.trydev.aulaunair.ui.absen.model.Absen

class DetailAbsenAdapter(private val context: Context): RecyclerView.Adapter<DetailAbsenAdapter.ViewHolder>()  {

    private val listMahasiswa = mutableListOf<Absen>()

    fun updateListFile(listMahasiswa:MutableList<Absen>) {
        this.listMahasiswa.clear()
        this.listMahasiswa.addAll(listMahasiswa)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_absen, parent, false))
    }

    override fun getItemCount(): Int = listMahasiswa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listMahasiswa[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val namaMahasiswa = view.findViewById<TextView>(R.id.nama_mhs)
        private val tglAbsen = view.findViewById<TextView>(R.id.tanggal_absen)

        fun bindItem(item: Absen) {
            namaMahasiswa.text = item.nama
        }
    }
}
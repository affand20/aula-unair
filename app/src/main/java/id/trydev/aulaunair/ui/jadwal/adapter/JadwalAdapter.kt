package id.trydev.aulaunair.ui.jadwal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Matkul

class JadwalAdapter(private val context: Context):RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    private val listJadwal: MutableList<Matkul> = mutableListOf()

    fun updateJadwal(listJadwal: List<Matkul>) {
        this.listJadwal.clear()
        this.listJadwal.addAll(listJadwal)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_jadwal, parent, false))
    }

    override fun getItemCount(): Int = listJadwal.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listJadwal[position])
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        private val tvJudul = view.findViewById<TextView>(R.id.judul_matkul)
        private val tvJadwal = view.findViewById<TextView>(R.id.tv_jadwal)

        fun bindItem(matkul: Matkul) {
            tvJudul.text = matkul.judul
            val jam = StringBuilder()
            matkul.jadwal?.forEach {
                jam.append("${it.get("hari")}, ${it.get("jam")}\n")
            }
            tvJadwal.text = jam
        }
    }
}
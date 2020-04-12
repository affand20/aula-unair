package id.trydev.aulaunair.ui.materi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Matkul
import id.trydev.aulaunair.ui.tugas.DetailTugasActivity

class TugasAdapter(private val context:Context): RecyclerView.Adapter<TugasAdapter.ViewHolder>() {

    private val listMatkul = mutableListOf<Matkul>()

    fun updateList(listMatkul: MutableList<Matkul>) {
        this.listMatkul.clear()
        this.listMatkul.addAll(listMatkul)
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvJudul = view.findViewById<TextView>(R.id.judul_matkul)
        private val itemMatkul = view.findViewById<ConstraintLayout>(R.id.item_materi)

        fun bindItem(matkul: Matkul) {
            tvJudul.text = matkul.judul
            itemMatkul.setOnClickListener {
                context.startActivity(
                    Intent(context, DetailTugasActivity::class.java)
                        .putExtra("id", matkul.matkulId)
                        .putExtra("judul", matkul.judul)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_materi, parent, false))
    }

    override fun getItemCount(): Int = listMatkul.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listMatkul[position])
    }
}
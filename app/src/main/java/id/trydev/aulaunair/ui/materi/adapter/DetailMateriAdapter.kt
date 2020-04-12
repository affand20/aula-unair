package id.trydev.aulaunair.ui.materi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Materi

class DetailMateriAdapter(private val context: Context, val listener:(Materi)->Unit): RecyclerView.Adapter<DetailMateriAdapter.ViewHolder>() {

    private val listFile = mutableListOf<Materi>()

    fun updateListFile(listFile:MutableList<Materi>) {
        this.listFile.clear()
        this.listFile.addAll(listFile)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false))
    }

    override fun getItemCount(): Int = listFile.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listFile[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val namaFile = view.findViewById<TextView>(R.id.nama_file)
        private val itemFile = view.findViewById<MaterialCardView>(R.id.item_file)

        fun bindItem(item: Materi) {
            namaFile.text = item.nama
            itemFile.setOnClickListener {
                listener(item)
            }
        }
    }
}
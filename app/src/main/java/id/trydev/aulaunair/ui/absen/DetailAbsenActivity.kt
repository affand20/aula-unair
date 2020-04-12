package id.trydev.aulaunair.ui.absen

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.ui.absen.adapter.DetailAbsenAdapter
import id.trydev.aulaunair.ui.absen.model.Absen
import id.trydev.aulaunair.ui.jadwal.model.Mahasiswa
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_absen.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DetailAbsenActivity : AppCompatActivity() {

    private lateinit var prefs: AppPreferences

    private val listMahasiswa = mutableListOf<String>()
    private lateinit var spinnerMahasiswaAdapter: ArrayAdapter<String>

    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var adapter: DetailAbsenAdapter
    private val listAbsen = mutableListOf<Absen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_absen)

        nama_matkul.text = intent?.getStringExtra("judul")

        prefs = AppPreferences(this)

        adapter = DetailAbsenAdapter(this)

        rv_absen.layoutManager = LinearLayoutManager(this)
        rv_absen.adapter = adapter

        btn_input_absen.setOnClickListener {
            var mahasiswa = ""

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_absen, null)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)

            val alertDialog = builder.show()

            val spinnerMahasiswa = dialogView.findViewById<Spinner>(R.id.spinner_mahasiswa)

            spinnerMahasiswaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listMahasiswa)
            spinnerMahasiswaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerMahasiswa.adapter = spinnerMahasiswaAdapter


            val btnNext = dialogView.findViewById<Button>(R.id.btn_selanjutnya)

            btnNext.setOnClickListener {
                mahasiswa = spinnerMahasiswa.selectedItem.toString()

                val current = Date()
                val date = SimpleDateFormat("dd/MM/yyyy").format(current)

                mFirestore.collection("matkul")
                    .document(intent?.getStringExtra("id").toString())
                    .collection("absen")
                    .add(hashMapOf(
                        "nama" to mahasiswa,
                        "tanggal" to date
                    ))
                    .addOnSuccessListener {
                        getData()
                        alertDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Log.d("DETAIL_ABSEN", it.localizedMessage)
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

            }
        }

        if (prefs.role == 1) {
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.biru))
            bg_top_img.setImageResource(R.drawable.gelombang)
            nama_matkul.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else if (prefs.role == 2) {
            Utils.hideView(btn_input_absen)
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.kuning))
            bg_top_img.setImageResource(R.drawable.gelombang2)
            nama_matkul.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }

        getData()
    }

    private fun getData() {
        listMahasiswa.clear()
        listAbsen.clear()
        Utils.showView(progress_bar)
        Utils.hideView(rv_absen)
        Utils.hideView(empty_state)

        val ref = mFirestore.collection("matkul")
            .document(intent?.getStringExtra("id").toString())

        if (prefs.role == 1) {
            ref.collection("mahasiswa")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val mahasiswa = it.toObject(Mahasiswa::class.java)
                        listMahasiswa.add(mahasiswa.nama.toString())
                    }
                }
        }

        ref.collection("absen")
            .get()
            .addOnSuccessListener {
                Utils.hideView(progress_bar)
                it.forEach {
                    val absen = it.toObject(Absen::class.java)
                    listAbsen.add(absen)
                }

                if (listAbsen.size > 0) {
                    adapter.updateListFile(listAbsen)
                    Utils.showView(rv_absen)
                } else {
                    Utils.showView(empty_state)
                }
            }
            .addOnFailureListener {
                Utils.hideView(progress_bar)
                Utils.showView(empty_state)

                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}

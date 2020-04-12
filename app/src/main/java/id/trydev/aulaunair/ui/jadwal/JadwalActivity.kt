package id.trydev.aulaunair.ui.jadwal

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.ui.jadwal.adapter.JadwalAdapter
import id.trydev.aulaunair.model.Matkul
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_jadwal.*

class JadwalActivity : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val listJadwal: MutableList<Matkul> = mutableListOf()

    private lateinit var jadwalAdapter: JadwalAdapter

    private lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        prefs = AppPreferences(this)

        jadwalAdapter = JadwalAdapter(this)

        rv_jadwal.layoutManager = LinearLayoutManager(this)
        rv_jadwal.adapter = jadwalAdapter

        if (prefs.role == 1) {
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.biru))
            bg_top_img.setImageResource(R.drawable.gelombang)
            pg_title.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            getJadwalDosen()
        } else if (prefs.role == 2) {
            getJadwalMahasiswa()
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.kuning))
            bg_top_img.setImageResource(R.drawable.gelombang2)
            pg_title.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }


    }

    private fun getJadwalDosen() {
        listJadwal.clear()
        Utils.showView(progress_bar)

        mFirestore.collection("matkul")
            .whereEqualTo("dosenId", mAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener {querySnapshot ->
                querySnapshot.forEach {
                    val matkul = it.toObject(Matkul::class.java)
                    listJadwal.add(matkul)
                }
                Utils.hideView(progress_bar)
                jadwalAdapter.updateJadwal(listJadwal)
            }
            .addOnFailureListener {
                Utils.hideView(progress_bar)
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun getJadwalMahasiswa() {
        listJadwal.clear()
        Utils.showView(progress_bar)

        mFirestore.collectionGroup("mahasiswa")
            .whereEqualTo("mahasiswaId", mAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener {querySnapshot ->
                querySnapshot.forEach {doc ->

                    doc.reference.parent.parent?.get()
                        ?.addOnSuccessListener {
                            val matkul = it.toObject(Matkul::class.java)
                            if (matkul!=null) {
                                Log.d("JADWAL_MAHASISWA", matkul.judul.toString())
                                listJadwal.add(matkul)
                            }
                            Utils.hideView(progress_bar)
                            jadwalAdapter.updateJadwal(listJadwal)
                        }
                        ?.addOnFailureListener {
                            Utils.hideView(progress_bar)
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                            Log.d("JADWAL_MAHASISWA", it.localizedMessage)
                        }
                }
            }
            .addOnFailureListener {
                Utils.hideView(progress_bar)
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                Log.d("JADWAL_MAHASISWA", it.localizedMessage)
            }
    }
}

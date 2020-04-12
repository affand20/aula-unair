package id.trydev.aulaunair.ui.absen

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Matkul
import id.trydev.aulaunair.ui.absen.adapter.AbsenAdapter
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_absen.*

class AbsenActivity : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var prefs: AppPreferences

    private val listMatkul = mutableListOf<Matkul>()
    private lateinit var adapter: AbsenAdapter

    private val listJudulMatkul = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        prefs = AppPreferences(this)

        adapter = AbsenAdapter(this)
        rv_materi.layoutManager = LinearLayoutManager(this)
        rv_materi.adapter = adapter


        if (prefs.role==1) {
            getData()
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.biru))
            bg_top_img.setImageResource(R.drawable.gelombang)
            pg_title.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else if (prefs.role == 2) {
            getDataMahasiswa()
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.kuning))
            bg_top_img.setImageResource(R.drawable.gelombang2)
            pg_title.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
    }

    private fun getDataMahasiswa() {
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
                                matkul.matkulId = it.id
                                listMatkul.add(matkul)
                            }
                            Utils.hideView(progress_bar)
                            adapter.updateList(listMatkul)
                        }
                        ?.addOnFailureListener {
                            Utils.hideView(progress_bar)
                            Toast.makeText(this, "Error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Utils.hideView(progress_bar)
                Toast.makeText(this, "Error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getData() {
        Utils.showView(progress_bar)
        mFirestore.collection("matkul")
            .whereEqualTo("dosenId", mAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener {querySnapshot ->
                querySnapshot.forEach {
                    val matkul = it.toObject(Matkul::class.java)
                    matkul.matkulId = it.id
                    listMatkul.add(matkul)
                    listJudulMatkul.add(matkul.judul.toString())
                }
                adapter.updateList(listMatkul)
                Utils.hideView(progress_bar)
            }
            .addOnFailureListener {
                Utils.hideView(progress_bar)
                Toast.makeText(this, "Error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}

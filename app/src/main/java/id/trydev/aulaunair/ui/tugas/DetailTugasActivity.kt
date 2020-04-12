package id.trydev.aulaunair.ui.tugas

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Materi
import id.trydev.aulaunair.ui.materi.adapter.DetailTugasAdapter
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_tugas.*

class DetailTugasActivity : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: DetailTugasAdapter
    private val listFile = mutableListOf<Materi>()

    private lateinit var prefs:AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tugas)

        nama_matkul.text = intent?.getStringExtra("judul")

        adapter = DetailTugasAdapter(this){
            Toast.makeText(this, "Download dimulali", Toast.LENGTH_SHORT).show()
            val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(it.url)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(applicationContext, "AULA", it.nama)
            downloadManager.enqueue(request)
        }

        prefs = AppPreferences(this)

        rv_file.layoutManager = LinearLayoutManager(this)
        rv_file.adapter = adapter

        getData()

        if (prefs.role == 1) {
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.biru))
            bg_top_img.setImageResource(R.drawable.gelombang)
            nama_matkul.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else if (prefs.role == 2) {
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.kuning))
            bg_top_img.setImageResource(R.drawable.gelombang2)
            nama_matkul.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }

    }

    private fun getData() {
        Utils.showView(progress_bar)
        Utils.hideView(rv_file)
        Utils.hideView(empty_state)

        mFirestore.collection("matkul")
            .document(intent?.getStringExtra("id").toString())
            .collection("tugas")
            .get()
            .addOnSuccessListener {
                Utils.hideView(progress_bar)
                it.forEach {
                    val materi = it.toObject(Materi::class.java)
                    listFile.add(materi)
                }

                if (listFile.size > 0) {
                    adapter.updateListFile(listFile)
                    Utils.showView(rv_file)
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

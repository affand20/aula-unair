package id.trydev.aulaunair.ui.tugas

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Matkul
import id.trydev.aulaunair.model.Materi
import id.trydev.aulaunair.ui.materi.adapter.TugasAdapter
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_materi.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class TugasActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var matkul:String? = null
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private val listMatkul = mutableListOf<Matkul>()
    private val listJudulMatkul = mutableListOf<String>()

    private lateinit var adapter: TugasAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tugas)

        prefs = AppPreferences(this)

        adapter = TugasAdapter(this)
        rv_materi.layoutManager = LinearLayoutManager(this)
        rv_materi.adapter = adapter

        btn_upload_materi.setOnClickListener {
            storageTask()
        }

        if (prefs.role==1) {
            getData()
            bg_top.background = ColorDrawable(ContextCompat.getColor(this, R.color.biru))
            bg_top_img.setImageResource(R.drawable.gelombang)
            pg_title.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else if (prefs.role == 2) {
            Utils.hideView(btn_upload_materi)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcChoose && resultCode == Activity.RESULT_OK && data!=null && data.data!=null) {
            if (data.data != null) {
                Utils.hideView(rv_materi)
                Utils.showView(progress_bar)

                val uri = data.data
                val fileName = File(uri?.lastPathSegment.toString()).name

                val ref = mStorage.getReference("${matkul}/tugas/$fileName.pdf")
                ref.putFile(uri as Uri)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@continueWithTask ref.downloadUrl
                    }
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            val downloadUrl = it.result

                            val matkulId = listMatkul.filter {
                                it.judul == matkul
                            }

                            mFirestore.collection("matkul")
                                .whereEqualTo("judul", matkul)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    snapshot.forEach {
                                        it.reference.collection("tugas")
                                            .add(
                                                Materi(
                                                    fileName,
                                                    downloadUrl.toString()
                                                )
                                            )
                                            .addOnSuccessListener {
                                                Utils.hideView(progress_bar)
                                                Utils.showView(rv_materi)
                                            }
                                            .addOnFailureListener {
                                                Utils.hideView(progress_bar)
                                                Utils.showView(rv_materi)
                                                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                                                Log.d(TAG, it.localizedMessage)
                                            }
                                    }
                                }

                        } else {
                            Log.d(TAG, it.exception?.localizedMessage.toString())
                            Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @AfterPermissionGranted(rcStorage)
    private fun storageTask() {
        if (hasStoragePermission()) {

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_pilih_mata_kuliah, null)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)

            val alertDialog = builder.show()

            val spinner = dialogView.findViewById<Spinner>(R.id.spinner_matkul)

            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listJudulMatkul)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = spinnerAdapter


            val btnNext = dialogView.findViewById<Button>(R.id.btn_selanjutnya)

            btnNext.setOnClickListener {
                matkul = spinner.selectedItem.toString()
                alertDialog.dismiss()

                // buka file manager buat pilih file PDF
                val intent = Intent()
                intent.setType("application/pdf")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Pilih File"), rcChoose)
            }


        } else {
            EasyPermissions.requestPermissions(
                this,
                "Aula UNAIR butuh akses ke dokumen anda.",
                rcStorage,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun hasStoragePermission():Boolean = EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    companion object {
        const val TAG = "UploadMateriActivity"
        const val rcStorage = 100
        const val rcChoose = 101
    }
}

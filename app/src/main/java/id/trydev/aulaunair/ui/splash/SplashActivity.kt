package id.trydev.aulaunair.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Users
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.rainbow.RainbowConnectionListener
import id.trydev.aulaunair.ui.dosen.DosenActivity
import id.trydev.aulaunair.ui.login.LoginActivity
import id.trydev.aulaunair.ui.mahasiswa.MahasiswaActivity
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(), RainbowConnectionListener.Connection {

    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()

    lateinit var prefs:AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val hashMap = HashMap<String, String>()
        hashMap.put("kondisi", "baik")
        hashMap.put("tes", "tes")

        val dummy = Dummy()
        dummy.id = 1
        dummy.nama = "Tes"
        dummy.nilai = hashMap

        mFirestore.collection("dummy")
            .add(dummy)
            .addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }

        mFirestore.collection("dummy")
            .document("1234")
            .get()
            .addOnSuccessListener {
                val dummy = it.toObject(Dummy::class.java)
                Log.d("TAG", dummy?.nilai?.get("tes"))
            }
            .addOnFailureListener {
                Log.d("TAG", it.localizedMessage)
            }


//        prefs = AppPreferences(this)

//        Handler().postDelayed({
//            if (RainbowConnection.getConnectionState() == "RAINBOW_CONNECTIONCONNECTED") {
//                mFirestore.collection("users")
//                    .document(mAuth.currentUser?.uid.toString())
//                    .get()
//                    .addOnSuccessListener {
//                        Utils.hideView(progress_bar)
//                        val user = it.toObject(Users::class.java)
//                        when (user?.role) {
//                            1 -> {
//                                val intent = Intent(this, DosenActivity::class.java)
//                                startActivity(intent)
//                            }
//                            2 -> {
//                                val intent = Intent(this, MahasiswaActivity::class.java)
//                                startActivity(intent)
//                            }
//                        }
//                        finish()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
//                        Utils.hideView(progress_bar)
//                    }
//            } else {
//                RainbowConnection.startConnection(this)
//            }
//        }, 2500)
    }

    override fun onConnectionSuccess() {
        progress_bar.visibility = View.GONE
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onConnectionFailed(error: String) {
        progress_bar.visibility = View.GONE
        Toast.makeText(this, "Error: $error.\nKeluar dari aplikasi dalam 3 detik", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            finish()
        }, 3000)
    }
}

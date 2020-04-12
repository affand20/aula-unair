package id.trydev.aulaunair.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ale.rainbowsdk.RainbowSdk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.aulaunair.R
import id.trydev.aulaunair.model.Users
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.rainbow.RainbowConnectionListener
import id.trydev.aulaunair.ui.dosen.DosenActivity
import id.trydev.aulaunair.ui.mahasiswa.MahasiswaActivity
import id.trydev.aulaunair.ui.splash.SplashActivity
import id.trydev.aulaunair.utils.AppPreferences
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), RainbowConnectionListener.Login {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var prefs : AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = AppPreferences(this)

        Log.d("STATE_DOSEN_ACTIVITY", "${RainbowSdk.instance().connection().state}")

        btn_login.setOnClickListener {
            if (validate()) {
                val email = edt_email.text.toString()
                val password = edt_passworc.text.toString()

                Utils.showView(progress_bar)

                RainbowConnection.startSignIn(email, password, this)
            }
        }
    }

    private fun validate():Boolean {
        if (edt_email.text.toString().isEmpty()) {
            edt_email.error = "Wajib diisi"
            edt_email.requestFocus()
            return false
        }
        if (edt_passworc.text.toString().isEmpty()) {
            edt_passworc.error = "Wajib diisi"
            edt_passworc.requestFocus()
            return false
        }
        return true
    }

    override fun onSignInSuccess(email:String, password: String) {
        Utils.showView(progress_bar)
        firebaseLogin(email, password)
    }

    override fun onSignInFailed(error: String) {
        Utils.hideView(progress_bar)
        Toast.makeText(this, "Error $error", Toast.LENGTH_SHORT).show()
    }

    private fun firebaseLogin(email:String, password:String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                firebaseFirestore.collection("users")
                    .document(firebaseAuth.currentUser?.uid.toString())
                    .get()
                    .addOnSuccessListener {
                        Utils.hideView(progress_bar)
                        val user = it.toObject(Users::class.java)
                        when (user?.role) {
                            1 -> {
                                prefs.role = 1
                                val intent = Intent(this, DosenActivity::class.java)
                                startActivity(intent)
                            }
                            2 -> {
                                prefs.role = 2
                                val intent = Intent(this, MahasiswaActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
                        Utils.hideView(progress_bar)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
                Utils.hideView(progress_bar)
            }
    }

}

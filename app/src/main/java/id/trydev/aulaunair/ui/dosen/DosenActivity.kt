package id.trydev.aulaunair.ui.dosen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ale.listener.SignoutResponseListener
import com.ale.rainbowsdk.RainbowSdk
import com.google.firebase.auth.FirebaseAuth
import id.trydev.aulaunair.R
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.ui.absen.AbsenActivity
import id.trydev.aulaunair.ui.channel.ChannelActivity
import id.trydev.aulaunair.ui.chat.ContactListActivity
import id.trydev.aulaunair.ui.jadwal.JadwalActivity
import id.trydev.aulaunair.ui.login.LoginActivity
import id.trydev.aulaunair.ui.materi.MateriActivity
import id.trydev.aulaunair.ui.splash.SplashActivity
import id.trydev.aulaunair.ui.tugas.TugasActivity
import id.trydev.aulaunair.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_dosen.*
import kotlinx.android.synthetic.main.layout_menu_dosen.*

class DosenActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dosen)

        prefs = AppPreferences(this)

        tugas.text = "Upload Tugas"
        materi.text = "Upload Materi"
        absen.text = "Absen Mahasiswa"

        val nama = RainbowConnection.getFullName()
        greeting.text = String.format(getString(R.string.greeting), nama)

        ib_message.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
        }

        ib_sign_out.setOnClickListener {
            RainbowSdk.instance().connection().signout(object: SignoutResponseListener(){
                override fun onSignoutSucceeded() {
                    mAuth.signOut()
                    prefs.resetPreference()

                    startActivity(Intent(this@DosenActivity, SplashActivity::class.java))
                    finish()
                }
            })
        }

        ib_channel.setOnClickListener {
            val intent = Intent(this, ChannelActivity::class.java)
            startActivity(intent)
        }

        cv_materi.setOnClickListener {
            val intent = Intent(this, MateriActivity::class.java)
            startActivity(intent)
        }

        cv_absensi.setOnClickListener {
            val intent = Intent(this, AbsenActivity::class.java)
            startActivity(intent)
        }

        cv_jadwal.setOnClickListener {
            val intent = Intent(this, JadwalActivity::class.java)
            startActivity(intent)
        }

        cv_tugas.setOnClickListener {
            val intent = Intent(this, TugasActivity::class.java)
            startActivity(intent)
        }
    }
}

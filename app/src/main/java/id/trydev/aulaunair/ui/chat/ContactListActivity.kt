package id.trydev.aulaunair.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ale.infra.contact.IRainbowContact
import com.ale.infra.list.IItemListChangeListener
import com.ale.rainbowsdk.RainbowSdk
import id.trydev.aulaunair.R
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.rainbow.RainbowConnectionListener
import id.trydev.aulaunair.ui.chat.adapter.ContactListAdapter
import kotlinx.android.synthetic.main.activity_contact_list.*

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var contacts: List<IRainbowContact>
    private val contactChangeListener =
        IItemListChangeListener { RainbowConnection.registerContactChangeListener(contactListener) }
    private val contactListener = object: RainbowConnectionListener.Contact() {
        override fun contactUpdated() {
            contacts =  RainbowConnection.rainbowContacts
            contacts.forEach {
                Log.d("LOG_CONTACT", "${it.firstName} ${it.lastName}")
            }
            contactListAdapter.submitList(contacts)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        contactListAdapter = ContactListAdapter(this)

        rv_user_list.layoutManager = LinearLayoutManager(this)
        rv_user_list.adapter = contactListAdapter

        RainbowConnection.registerAllRainbowContact(contactChangeListener)

        getData()
    }

    private fun getData() {
        RainbowConnection.rainbowContacts.let {
            contactListAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RainbowConnection.unregisterContactChangeListener(contactListener)
        RainbowConnection.unregisterAllRainbowContact(contactChangeListener)
    }
}

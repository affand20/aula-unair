package id.trydev.aulaunair.rainbow

import android.os.Handler
import android.os.Looper
import com.ale.infra.contact.IRainbowContact
import com.ale.infra.contact.RainbowPresence

class RainbowConnectionListener {

    abstract class Contact: IRainbowContact.IContactListener{
        override fun onCompanyChanged(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPresenceChanged(p0: IRainbowContact?, p1: RainbowPresence?) {
            Handler(Looper.getMainLooper())
                .post { contactUpdated() }
        }

        override fun onActionInProgress(p0: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun contactUpdated(p0: IRainbowContact?) {
            Handler(Looper.getMainLooper())
                .post { contactUpdated() }
        }

        abstract fun contactUpdated()
    }

    interface Connection {
        fun onConnectionSuccess()

        fun onConnectionFailed(error:String)
    }

    interface Login {
        fun onSignInSuccess(email:String, password:String)

        fun onSignInFailed(error:String)
    }

}
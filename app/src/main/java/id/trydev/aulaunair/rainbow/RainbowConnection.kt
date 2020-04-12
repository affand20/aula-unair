package id.trydev.aulaunair.rainbow

import android.os.Handler
import android.os.Looper
import com.ale.infra.contact.IRainbowContact
import com.ale.infra.list.IItemListChangeListener
import com.ale.infra.manager.channel.Channel
import com.ale.infra.proxy.conversation.IRainbowConversation
import com.ale.listener.IConnectionChanged
import com.ale.listener.SigninResponseListener
import com.ale.listener.StartResponseListener
import com.ale.rainbowsdk.RainbowSdk

object RainbowConnection {

    val rainbowContacts: List<IRainbowContact>
        get() = RainbowSdk.instance().contacts().rainbowContacts.copyOfDataList

    val allChannels: List<Channel>
        get() = RainbowSdk.instance().channels().allChannels.copyOfDataList

    /*
    * Menjalankan service Rainbow agar fungsi seperti Sign In bisa digunakan.
    * */
    fun startConnection(connection: RainbowConnectionListener.Connection) {
        RainbowSdk.instance().connection().start(object : StartResponseListener() {
            override fun onRequestFailed(p0: RainbowSdk.ErrorCode?, p1: String) {
                Handler(Looper.getMainLooper())
                    .post{ connection.onConnectionFailed(p1) }
            }

            override fun onStartSucceeded() {
                Handler(Looper.getMainLooper())
                    .post(connection::onConnectionSuccess)
            }

        })
    }

    /*
    * Masuk / Sign In (masuk) ke platform Rainbow.
    * */
    fun startSignIn(email:String, password:String, login: RainbowConnectionListener.Login) {
        RainbowSdk.instance().connection()
            .signin(email, password, "sandbox.openrainbow.com", object : SigninResponseListener() {
                override fun onRequestFailed(p0: RainbowSdk.ErrorCode?, p1: String) {
                    Handler(Looper.getMainLooper())
                        .post { login.onSignInFailed(p1) }
                }

                override fun onSigninSucceeded() {
                    Handler(Looper.getMainLooper())
                        .post{ login.onSignInSuccess(email, password) }
                }
            })
    }

    /*
    * Mendapatkan data percakapan dari kontak yang dipilih.
    * */
    fun getConversationFromContact(contactJid:String?):IRainbowConversation =
        RainbowSdk.instance().conversations().getConversationFromContact(contactJid)

    /*
    * Mendapatkan daftar pesan dari data percakapan.
    * */
    fun getMessagesFromConversation(conversation: IRainbowConversation) =
        RainbowSdk.instance().im().getMessagesFromConversation(conversation, 100)

    /*
    * Mengirimkan data pesan ke data percakapan.
    * */
    fun sendMessageToConversation(conversation: IRainbowConversation, message:String) =
        RainbowSdk.instance().im().sendMessageToConversation(conversation, message)

    /*
    * Mendaftarkan semua kontak ke dalam listener agar jika terjadi perubahan dapat
    * langsung ditampilkan secara realtime.
    * */
    fun registerAllRainbowContact(listener: IItemListChangeListener) =
        RainbowSdk.instance().contacts().rainbowContacts.registerChangeListener(listener)

    /*
    * Menghapus kontak yang terdaftar pada listener agar tidak terjadi Leaks (kebocoran)
    * pada memory.
    * */
    fun unregisterAllRainbowContact(listener: IItemListChangeListener) =
        RainbowSdk.instance().contacts().rainbowContacts.unregisterChangeListener(listener)

    fun registerContactChangeListener(listener: IRainbowContact.IContactListener) {
        rainbowContacts.forEach {
            it.registerChangeListener(listener)
        }
    }

    fun unregisterContactChangeListener(listener: IRainbowContact.IContactListener) {
        rainbowContacts.forEach {
            it.unregisterChangeListener(listener)
        }
    }

    fun getConnectionState() = RainbowSdk.instance().connection().state.toString()

    fun getFullName() = "${RainbowSdk.instance().myProfile().connectedUser.firstName} ${RainbowSdk.instance().myProfile().connectedUser.lastName}"

    fun registerChannelChangeListener(listener: IItemListChangeListener) =
        RainbowSdk.instance().channels().allChannels.registerChangeListener(listener)

    fun unregisterChannelChangeListener(listener: IItemListChangeListener) =
        RainbowSdk.instance().channels().allChannels.unregisterChangeListener(listener)

    fun registerAllChannel(listener: Channel.IChannelListener) {
        for (channel in allChannels) {
            channel.registerListener(listener)
            channel.mode = Channel.ChannelMode.ALL_PUBLIC
        }
    }

    fun unregisterAllChannel(listener: Channel.IChannelListener) {
        for (channel in allChannels) {
            channel.unregisterListener(listener)
            channel.mode = Channel.ChannelMode.ALL_PUBLIC
        }
    }

}
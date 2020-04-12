package id.trydev.aulaunair.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ale.infra.list.IItemListChangeListener
import com.ale.infra.proxy.conversation.IRainbowConversation
import id.trydev.aulaunair.R
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.ui.chat.adapter.ChatListAdapter
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity(), IItemListChangeListener {

    private lateinit var contactJId:String
    private lateinit var username:String
    private lateinit var chatListAdapter: ChatListAdapter

    private lateinit var conversation: IRainbowConversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        contactJId = intent.getStringExtra(ARG_CONTACT_ID) as String
        username = intent.getStringExtra(ARG_USERNAME) as String

        btn_back.setOnClickListener {
            finish()
        }

        tv_nama_kontak.text = username

        chatListAdapter =
            ChatListAdapter(mutableListOf())
        rv_chat_list.layoutManager = LinearLayoutManager(this)
        rv_chat_list.adapter = chatListAdapter

        conversation = RainbowConnection.getConversationFromContact(contactJId)
        conversation.messages.registerChangeListener(this)

        RainbowConnection.getMessagesFromConversation(conversation)

        btn_send_message.setOnClickListener {
            val message = edt_message_input.text.toString()

            RainbowConnection.sendMessageToConversation(conversation, message)
            edt_message_input.setText("")
            rv_chat_list.smoothScrollToPosition(conversation.messages.copyOfDataList.lastIndex)
        }
    }

    companion object {
        const val ARG_CONTACT_ID = "contact_id"
        const val ARG_USERNAME = "username"
    }

    override fun dataChanged() {
        runOnUiThread {
            chatListAdapter.submitList(conversation.messages.copyOfDataList)
            if (conversation.messages.count > 0) {
                rv_chat_list.smoothScrollToPosition(conversation.messages.copyOfDataList.lastIndex)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conversation.messages.unregisterChangeListener(this)
    }
}

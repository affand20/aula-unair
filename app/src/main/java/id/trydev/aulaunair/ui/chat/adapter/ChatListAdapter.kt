package id.trydev.aulaunair.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.manager.IMMessage
import id.trydev.aulaunair.R
import java.lang.IllegalArgumentException

class ChatListAdapter(private val messages: MutableList<IMMessage>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    fun submitList(updatedMessage:MutableList<IMMessage>) {
        messages.clear()
        messages.addAll(updatedMessage)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.isMsgSent) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_received, parent, false)
                ChatReceivedViewHolder(view)
            }
            VIEW_TYPE_MESSAGE_SENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_send, parent,false)
                ChatSendViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unkown View Type")
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ChatSendViewHolder -> {
                holder.bindSenderContent(messages[position])
            }

            is ChatReceivedViewHolder -> {
                holder.bindReceivedContent(messages[position])
            }
        }
    }

    inner class ChatReceivedViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val textMessage = view.findViewById<TextView>(R.id.tv_received_message)
        private val receivedTime = view.findViewById<TextView>(R.id.tv_received_time)

        fun bindReceivedContent(message:IMMessage) {
            textMessage.text = message.messageContent
//            receivedTime.text = message.messageDateReceived.time.toString()
        }
    }

    inner class ChatSendViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val textMessage = view.findViewById<TextView>(R.id.tv_sender_message)
        private val senderdTime = view.findViewById<TextView>(R.id.tv_sender_time)

        fun bindSenderContent(message:IMMessage) {
            textMessage.text = message.messageContent
//            senderdTime.text = message.messageDate.time.toString()
        }
    }
}
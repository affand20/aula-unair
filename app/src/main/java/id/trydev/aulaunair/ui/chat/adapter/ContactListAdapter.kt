package id.trydev.aulaunair.ui.chat.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.contact.IRainbowContact
import com.ale.infra.contact.RainbowPresence
import de.hdodenhof.circleimageview.CircleImageView
import id.trydev.aulaunair.R
import id.trydev.aulaunair.ui.chat.ChatRoomActivity
import id.trydev.aulaunair.ui.chat.ChatRoomActivity.Companion.ARG_CONTACT_ID
import id.trydev.aulaunair.ui.chat.ChatRoomActivity.Companion.ARG_USERNAME

class ContactListAdapter(private val context: Context):ListAdapter<IRainbowContact, ContactListAdapter.ContactViewHolder>(
    object : DiffUtil.ItemCallback<IRainbowContact>() {
        override fun areItemsTheSame(oldItem: IRainbowContact, newItem: IRainbowContact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: IRainbowContact,
            newItem: IRainbowContact
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvUserName = view.findViewById<TextView>(R.id.txt_user_name)
        private val tvUserStatus = view.findViewById<TextView>(R.id.txt_user_status)
        private val imgUserStatus = view.findViewById<CircleImageView>(R.id.img_user_status)
        private val imgUserPhoto = view.findViewById<CircleImageView>(R.id.img_user_photo)
        private val layout = view.findViewById<ConstraintLayout>(R.id.item_layout)

        fun bind(contact: IRainbowContact) {
            val userFullName = "${contact.firstName} ${contact.lastName}"
            val userPhoto = contact.photo

            tvUserName.text = userFullName
            imgUserPhoto.setImageBitmap(userPhoto)

            layout.setOnClickListener {
                val intent = Intent(it.context, ChatRoomActivity::class.java)
                intent.putExtra(ARG_CONTACT_ID, contact.jid)
                intent.putExtra(ARG_USERNAME, userFullName)

                it.context.startActivity(intent)
            }

            when (contact.presence) {
                RainbowPresence.ONLINE -> {
                    tvUserStatus.text = context.getString(R.string.status_online)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.GREEN))
                }
                RainbowPresence.OFFLINE -> {
                    tvUserStatus.text = context.getString(R.string.status_offline)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.GRAY))
                }
                RainbowPresence.MOBILE_ONLINE -> {
                    tvUserStatus.text = context.getString(R.string.status_on_mobile)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.BLUE))
                }
                RainbowPresence.AWAY -> {
                    tvUserStatus.text = context.getString(R.string.status_away)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.YELLOW))
                }
                RainbowPresence.BUSY -> {
                    tvUserStatus.text = context.getString(R.string.status_busy)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.CYAN))
                }
                RainbowPresence.DND -> {
                    tvUserStatus.text = context.getString(R.string.status_do_not_distrub)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.RED))
                }
                RainbowPresence.DND_PRESENTATION -> {
                    tvUserStatus.text = context.getString(R.string.status_presenting)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.RED))
                }
                else -> {
                    tvUserStatus.text = context.getString(R.string.status_unknown)
                    imgUserStatus.setImageDrawable(ColorDrawable(Color.GRAY))
                }
            }
        }
    }
}

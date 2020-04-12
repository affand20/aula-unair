package id.trydev.aulaunair.ui.channel.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.manager.channel.Channel
import id.trydev.aulaunair.R
import id.trydev.aulaunair.ui.channel.ChannelDetailActivity
import java.io.Serializable
import java.util.ArrayList

class ChannelAdapter(private val context: Context): RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private val listChannel = mutableListOf<Channel>()

    fun updateList(listChannel: List<Channel>) {
        this.listChannel.clear()
        this.listChannel.addAll(listChannel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false))
    }

    override fun getItemCount(): Int = listChannel.size

    override fun onBindViewHolder(holder: ChannelAdapter.ViewHolder, position: Int) {
        holder.bindItem(listChannel[position])
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        private val channelName = view.findViewById<TextView>(R.id.tv_channel_name)
        private val channelDesc = view.findViewById<TextView>(R.id.tv_channel_description)
        private val itemChannel = view.findViewById<CardView>(R.id.layout_item_channel)

        fun bindItem(channel: Channel) {
            channelName.text = channel.name
            channelDesc.text = channel.description
            itemChannel.setOnClickListener {
                context.startActivity(Intent(context, ChannelDetailActivity::class.java).putExtra("channelId", channel.id))
            }
        }
    }

}
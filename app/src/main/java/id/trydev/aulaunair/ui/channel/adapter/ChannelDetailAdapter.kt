package id.trydev.aulaunair.ui.channel.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.list.ArrayItemList
import com.ale.infra.manager.channel.ChannelItem

class ChannelDetailAdapter(private val context: Context): RecyclerView.Adapter<ChannelDetailAdapter.ViewHolder>() {

    private var listChannel = ArrayItemList<ChannelItem>()

    fun updateListChannel(listChannel:ArrayItemList<ChannelItem>) {
        this.listChannel = listChannel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = listChannel.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }
}
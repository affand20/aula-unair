package id.trydev.aulaunair.ui.channel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ale.rainbowsdk.RainbowSdk
import id.trydev.aulaunair.R
import id.trydev.aulaunair.ui.channel.adapter.ChannelDetailAdapter
import id.trydev.aulaunair.utils.Utils
import kotlinx.android.synthetic.main.activity_channel_detail.*

class ChannelDetailActivity : AppCompatActivity() {

    private lateinit var adapter: ChannelDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_detail)

        adapter = ChannelDetailAdapter(this)

        val channelId = intent?.getSerializableExtra("channelId")
        if (channelId!=null) {
            Log.d("CHANNEL_DETAIL", "$channelId")
        }

        rv_channel_content.layoutManager = LinearLayoutManager(this)
        rv_channel_content.adapter = adapter


        channel_name.text = getChannelName(channelId.toString())

        val listContent = RainbowSdk.instance().channels().getChannel(channelId.toString()).channelItems

        if (listContent.count > 0) {
            Utils.showView(rv_channel_content)
            Utils.hideView(state_empty)
            adapter.updateListChannel(listContent)
        } else {
            Utils.showView(state_empty)
            Utils.hideView(rv_channel_content)
        }

    }

    private fun getChannelName(channelId:String): String =
        RainbowSdk.instance().channels().getChannel(channelId).name

    private fun getChannelMsg(channelId: String) {
        RainbowSdk.instance().channels().getChannel(channelId).channelItems
    }

}

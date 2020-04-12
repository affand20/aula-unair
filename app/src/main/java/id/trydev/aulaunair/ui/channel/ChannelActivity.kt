package id.trydev.aulaunair.ui.channel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ale.rainbowsdk.RainbowSdk
import id.trydev.aulaunair.R
import id.trydev.aulaunair.rainbow.RainbowConnection
import id.trydev.aulaunair.ui.channel.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.activity_channel.*

class ChannelActivity : AppCompatActivity() {

    private lateinit var adapter: ChannelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        adapter = ChannelAdapter(this)

        rv_channel.layoutManager = LinearLayoutManager(this)
        rv_channel.adapter = adapter

        getChannels()


    }

    private fun getChannels() {
        RainbowConnection.allChannels.let {
            Log.d("GET_CHANNEL", "${it.size}")
            adapter.updateList(it)
        }
    }
}

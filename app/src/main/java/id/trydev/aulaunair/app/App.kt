package id.trydev.aulaunair.app

import android.app.Application
import com.ale.rainbowsdk.RainbowSdk

class App: Application() {

    private val appID = "44ee34a057aa11eabb3887f44e39165a"
    private val appSecret = "iE6FV0n5OBcnvHOjXqeL4YLwZJfrekmgOxQ7AbfqQ3j2rDCupKsKk1cU3EL0l7Jn"

    override fun onCreate() {
        super.onCreate()

        RainbowSdk.instance().initialize(this, appID, appSecret)
    }

}
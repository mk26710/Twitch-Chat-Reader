package dev.kadosawa.ttvreader.data

import com.gikk.twirk.Twirk
import com.gikk.twirk.TwirkBuilder
import dev.kadosawa.ttvreader.config.ModConfig
import dev.kadosawa.ttvreader.listeners.ChatListener
import me.shedaniel.autoconfig.AutoConfig


object Store {
    var channelName: String? = null
    var twirk: Twirk? = null

    fun reset() {
        channelName = null

        if (twirk?.isConnected == true) {
            twirk?.close()
        }

        twirk = null
    }

    fun buildTwirk() {
        val config = AutoConfig.getConfigHolder(ModConfig::class.java).config

        twirk = TwirkBuilder(channelName, config.username, config.OAuthToken).build()
        twirk?.addIrcListener(ChatListener())
        twirk?.connect()
    }
}
package dev.kadosawa.ttvreader.data

import com.gikk.twirk.Twirk
import com.gikk.twirk.TwirkBuilder
import dev.kadosawa.ttvreader.config.ModConfig
import dev.kadosawa.ttvreader.listeners.ChatListener
import kotlinx.coroutines.Runnable
import me.shedaniel.autoconfig.AutoConfig
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    /*
    A fix to issue #2 (github)

    We need some asynchronous stuff here
    otherwise connect() blocks the game process
    */
    private suspend fun twirkConnect(): Boolean? {
        return suspendCoroutine { continuation ->
            Thread(Runnable {
                try {
                    if (twirk == null) {
                        continuation.resume(false)
                    } else {
                        val isConnected = twirk?.connect()
                        continuation.resume(isConnected)
                    }
                } catch (ex: Exception) {
                    continuation.resumeWithException(ex)
                }
            }).start()
        }
    }

    suspend fun buildTwirk(): Boolean {
        val config = AutoConfig.getConfigHolder(ModConfig::class.java).config

        twirk = TwirkBuilder(channelName, config.username, config.OAuthToken).build()
        twirk?.addIrcListener(ChatListener())

        return when (twirkConnect()) {
            true -> true
            else -> {
                // If connection fails, we have to reset store's state
                reset()
                return false
            }
        }
    }
}
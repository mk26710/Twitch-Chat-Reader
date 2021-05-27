package dev.kadosawa.ttvreader.listeners

import com.gikk.twirk.events.TwirkListener
import com.gikk.twirk.types.twitchMessage.TwitchMessage
import com.gikk.twirk.types.users.TwitchUser
import dev.kadosawa.ttvreader.config.ModConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

@Suppress("unused")
class ChatListener : TwirkListener {
    companion object {
        private val chatHud = MinecraftClient.getInstance().inGameHud.chatHud
    }

    private fun hex(num: Int): String {
        return num.toString(16).uppercase()
    }

    override fun onPrivMsg(sender: TwitchUser?, message: TwitchMessage?) {
        if (sender == null || message == null)
            return

        val config = AutoConfig.getConfigHolder(ModConfig::class.java).config
        if (config.HideTwitchChat)
            return

        // This is probably the worst approach but my brain is kinda melting
        val badges = arrayListOf<String>()
        if (sender.isMod) badges.add("{\"text\":\"${config.badges.moderators} \",\"color\":\"${config.colors.moderators}\"},")
        if (sender.isSub) badges.add("{\"text\":\"${config.badges.subscribers} \",\"color\":\"${config.colors.subscribers}\"},")

        val color = hex(sender.color)
        val username = "{\"text\":\"${sender.userName}\",\"color\":\"#$color\"},"
        val content = "{\"text\":\": ${message.content}\",\"color\":\"#FFFFFF\"}"

        val asJson = "[" + badges.joinToString(separator = "") + username + content + "]"
        val toSend = Text.Serializer.fromJson(asJson)

        chatHud.addMessage(toSend)
    }
}

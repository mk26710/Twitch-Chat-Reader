package dev.kadosawa.ttvreader.listeners

import com.gikk.twirk.events.TwirkListener
import com.gikk.twirk.types.twitchMessage.TwitchMessage
import com.gikk.twirk.types.users.TwitchUser
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

        val color = hex(sender.color)
        val username = sender.userName
        val content = message.content

        val asJson = "[\"\",{\"text\":\"<\"},{\"text\":\"$username\",\"color\":\"#$color\"},{\"text\":\"> $content\"}]"
        val toSend = Text.Serializer.fromJson(asJson)

        chatHud.addMessage(toSend)
    }
}

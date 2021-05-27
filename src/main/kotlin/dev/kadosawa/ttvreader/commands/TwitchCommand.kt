package dev.kadosawa.ttvreader.commands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.kadosawa.ttvreader.config.ModConfig
import dev.kadosawa.ttvreader.data.Store
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting


object TwitchCommand {
    private val chatHud = MinecraftClient.getInstance().inGameHud.chatHud

    @Suppress("UNUSED_PARAMETER")
    @Throws(CommandSyntaxException::class)
    fun config(ctx: CommandContext<ServerCommandSource>): Int {
        val currentScreen = MinecraftClient.getInstance().currentScreen
        val menuScreen = AutoConfig.getConfigScreen(ModConfig::class.java, currentScreen).get()

        MinecraftClient.getInstance().openScreen(menuScreen)
        return 0
    }

    @Throws(CommandSyntaxException::class)
    fun connect(ctx: CommandContext<ServerCommandSource>): Int {
        Store.reset()

        val newChannelName = ctx.getArgument("name", String::class.java)
        Store.channelName = newChannelName

        val message = LiteralText("Connected to ${Store.channelName} chat room.").formatted(Formatting.AQUA)
        chatHud.addMessage(message)

        Store.buildTwirk()
        return 0
    }

    @Suppress("UNUSED_PARAMETER")
    @Throws(CommandSyntaxException::class)
    fun disconnect(ctx: CommandContext<ServerCommandSource>): Int {
        val message =
            LiteralText("You have disconnected from the ${Store.channelName} chat room. \nSettings are back to initial state.")
                .formatted(Formatting.AQUA)
        Store.reset()

        chatHud.addMessage(message)
        return 0
    }
}

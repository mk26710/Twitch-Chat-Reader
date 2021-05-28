package dev.kadosawa.ttvreader

import com.mojang.brigadier.arguments.StringArgumentType.word
import dev.kadosawa.ttvreader.commands.TwitchCommand
import dev.kadosawa.ttvreader.config.ModConfig
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.argument

@Suppress("unused")
fun init() {
    AutoConfig.register(ModConfig::class.java) { definition, configClass ->
        GsonConfigSerializer(definition, configClass)
    }

    CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, dedicated ->
        val twitchNode = CommandManager
            .literal("twitch")
            .build()

        val configNode = CommandManager
            .literal("config")
            .build()

        val configUsernameNode = CommandManager
            .literal("username")
            .then(argument("name", word()).executes(TwitchCommand.Configurator::username))
            .build()

        val configOAuthNode = CommandManager
            .literal("oauth")
            .then(argument("token", word()).executes(TwitchCommand.Configurator::oauth))
            .build()

        val connectNode = CommandManager
            .literal("connect")
            .then(argument("name", word()).executes(TwitchCommand::connect))
            .build()

        val resetNode = CommandManager
            .literal("disconnect")
            .executes(TwitchCommand::disconnect)
            .build()

        dispatcher.root.addChild(twitchNode)

        twitchNode.addChild(configNode)
        configNode.addChild(configUsernameNode)
        configNode.addChild(configOAuthNode)

        twitchNode.addChild(connectNode)
        twitchNode.addChild(resetNode)
    })
}


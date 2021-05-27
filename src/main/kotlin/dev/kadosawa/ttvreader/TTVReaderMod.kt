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
            .executes(TwitchCommand::config)
            .build()

        val readNode = CommandManager
            .literal("connect")
            .then(argument("name", word()).executes(TwitchCommand::connect))
            .build()

        val resetNode = CommandManager
            .literal("disconnect")
            .executes(TwitchCommand::disconnect)
            .build()

        dispatcher.root.addChild(twitchNode)
        twitchNode.addChild(configNode)
        twitchNode.addChild(readNode)
        twitchNode.addChild(resetNode)
    })
}


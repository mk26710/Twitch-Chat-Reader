package dev.kadosawa.ttvreader;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.kadosawa.ttvreader.commands.TwitchCommand;
import dev.kadosawa.ttvreader.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;

public class TTVReaderMod implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            final LiteralCommandNode<ServerCommandSource> twitchNode = CommandManager
                    .literal("twitch")
                    .build();

            final LiteralCommandNode<ServerCommandSource> connectNode = CommandManager
                    .literal("connect")
                    .then(argument("name", word()).executes(TwitchCommand::connect))
                    .build();

            final LiteralCommandNode<ServerCommandSource> disconnectNode = CommandManager
                    .literal("disconnect")
                    .executes(TwitchCommand::disconnect)
                    .build();

            final LiteralCommandNode<ServerCommandSource> configNode = CommandManager
                    .literal("config")
                    .build();

            final LiteralCommandNode<ServerCommandSource> configUsernameNode = CommandManager
                    .literal("username")
                    .then(argument("name", word()).executes(TwitchCommand.Configurator::username))
                    .build();

            final LiteralCommandNode<ServerCommandSource> configOAuthNode = CommandManager
                    .literal("oauth")
                    .then(argument("token", word()).executes(TwitchCommand.Configurator::oauth))
                    .build();

            dispatcher.getRoot().addChild(twitchNode);
            twitchNode.addChild(configNode);
            twitchNode.addChild(connectNode);
            twitchNode.addChild(disconnectNode);

            configNode.addChild(configUsernameNode);
            configNode.addChild(configOAuthNode);
        });
    }
}

package dev.kadosawa.ttvreader.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.kadosawa.ttvreader.config.ModConfig;
import dev.kadosawa.ttvreader.store.RealStore;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchCommand {
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    private static ChatHud getChat() {
        return MinecraftClient.getInstance().inGameHud.getChatHud();
    }

    public static int connect(CommandContext<ServerCommandSource> ctx) {
        // Before creating a new connection gotta close existing ones
        RealStore.initialState();

        // Keep requested name in the store
        final String newChannelName = ctx.getArgument("name", String.class);
        RealStore.setChannelName(newChannelName);

        // Prepare text messages
        final Text textStarted = new TranslatableText("commands.twitch.connect.started").formatted(Formatting.YELLOW);
        final Text textFailure = new TranslatableText("commands.twitch.connect.fail").formatted(Formatting.RED);
        final Text textSuccess = new TranslatableText("commands.twitch.connect.success", newChannelName).formatted(Formatting.YELLOW);

        getChat().addMessage(textStarted);

        RealStore.createTwirkConnection()
                .thenAcceptAsync((it) -> {
                    RealStore.LOGGER.debug("Entering next stage of twirk connection creation");

                    if (it) {
                        getChat().addMessage(textSuccess);
                        RealStore.LOGGER.debug("Twirk connection is successful");
                    } else {
                        getChat().addMessage(textFailure);
                        RealStore.LOGGER.debug("Twirk connection failed");
                    }
                }, executor);

        return 0;
    }

    @SuppressWarnings("unused")
    public static int disconnect(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        RealStore.initialState();
        getChat().addMessage(new TranslatableText("commands.twitch.reset.success").formatted(Formatting.YELLOW));
        return 0;
    }

    public final static class Configurator {
        public static int username(CommandContext<ServerCommandSource> ctx) {
            final String input = ctx.getArgument("name", String.class);

            final ConfigHolder<ModConfig> configHolder = AutoConfig.getConfigHolder(ModConfig.class);
            configHolder.getConfig().username = input;

            final Text msg = new TranslatableText("commands.twitch.config.username_updated", input).formatted(Formatting.YELLOW);

            CompletableFuture
                    .runAsync(configHolder::save, executor)
                    .thenRunAsync(() -> getChat().addMessage(msg), executor);

            return 0;
        }

        public static int oauth(CommandContext<ServerCommandSource> ctx) {
            final String input = ctx.getArgument("token", String.class);

            final ConfigHolder<ModConfig> configHolder = AutoConfig.getConfigHolder(ModConfig.class);
            configHolder.getConfig().OAuthToken = "oauth:" + input;

            final Text msg = new TranslatableText("commands.twitch.config.oauth_updated").formatted(Formatting.YELLOW);

            CompletableFuture
                    .runAsync(configHolder::save, executor)
                    .thenRunAsync(() -> getChat().addMessage(msg), executor);

            return 0;
        }
    }
}

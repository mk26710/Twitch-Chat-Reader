package dev.kadosawa.ttvreader.store;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import dev.kadosawa.ttvreader.config.ModConfig;
import dev.kadosawa.ttvreader.listeners.ChatListener;
import me.shedaniel.autoconfig.AutoConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RealStore {
    public final static Logger LOGGER = LogManager.getLogger();

    @Nullable
    private static String channelName = null;

    @Nullable
    private static Twirk twirk = null;

    public static @Nullable String getChannelName() {
        return channelName;
    }

    public static void setChannelName(@NotNull String value) {
        channelName = value;
    }

    public static @Nullable Twirk getTwirk() {
        return twirk;
    }

    public static void initialState() {
        channelName = null;

        if (twirk != null)
            twirk.close();

        twirk = null;
    }

    public static CompletableFuture<Boolean> createTwirkConnection() {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Running in the 90's");

            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            twirk = new TwirkBuilder(channelName, config.username, config.OAuthToken).build();
            twirk.addIrcListener(new ChatListener());

            try {
                return twirk.connect();
            } catch (IOException | InterruptedException e) {
                return false;
            }
        });
    }
}

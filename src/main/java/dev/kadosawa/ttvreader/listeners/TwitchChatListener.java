package dev.kadosawa.ttvreader.listeners;

import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import dev.kadosawa.ttvreader.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class TwitchChatListener implements TwirkListener {
    private static ChatHud getChat() {
        return MinecraftClient.getInstance().inGameHud.getChatHud();
    }

    public static int fromHEX(String hex) {
        return (Integer.decode(hex) | 0xFF_000000);
    }

    @Override
    public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
        if (sender == null || message == null)
            return;

        final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        if (config.HideTwitchChat)
            return;

        final MutableText toSend = new LiteralText("");

        if (sender.isMod() || sender.isOwner()) {
            final TextColor modColor = TextColor.fromRgb(fromHEX(config.colors.moderators));
            final Text modBadge = new LiteralText(config.badges.moderators + " ").styled(s -> s.withColor(modColor));
            toSend.append(modBadge);
        }

        if (sender.isSub()) {
            final TextColor subColor = TextColor.fromRgb(fromHEX(config.colors.subscribers));
            final Text subBadge = new LiteralText(config.badges.subscribers + " ").styled(s -> s.withColor(subColor));
            toSend.append(subBadge);
        }

        final TextColor userColor = TextColor.fromRgb(sender.getColor());
        final Text user = new LiteralText(sender.getDisplayName()).styled(s -> s.withColor(userColor));
        toSend.append(user);

        final Text msgContent = new LiteralText(": " + message.getContent()).styled(s -> s.withColor(Formatting.RESET));
        toSend.append(msgContent);

        getChat().addMessage(toSend);
    }
}

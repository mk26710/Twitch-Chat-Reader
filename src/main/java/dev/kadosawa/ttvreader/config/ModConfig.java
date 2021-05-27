package dev.kadosawa.ttvreader.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@SuppressWarnings("unused")
@Config(name = "ttvreader")
public class ModConfig implements ConfigData {
    public boolean HideTwitchChat = false;

    public String username = "";
    public String OAuthToken = "";

    @ConfigEntry.Gui.CollapsibleObject
    public Badges badges = new Badges();

    public static class Badges {
        public String moderators = "\uD83D\uDDE1";
        public String subscribers = "\u2764";
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Colors colors = new Colors();

    public static class Colors {
        public String moderators = "#00AD03";
        public String subscribers = "red";
    }
}
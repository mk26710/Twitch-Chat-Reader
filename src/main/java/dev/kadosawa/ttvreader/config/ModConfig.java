package dev.kadosawa.ttvreader.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@SuppressWarnings("unused")
@Config(name = "ttvreader")
public class ModConfig implements ConfigData {
    public String username = "";
    public String OAuthToken = "";
}
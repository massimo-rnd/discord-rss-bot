package gg.druffko.rssbot.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    public static String discordToken = "";
    public static String rssChannel = "";
    public static String postFile = "";
    public static String rssUrl = "";


    public static void getConfig(){
        String botConfigPath ="bot.properties";

        Properties botProperties = new Properties();
        try {
            botProperties.load(new FileInputStream(botConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        discordToken = botProperties.getProperty("discordToken");
        rssChannel = botProperties.getProperty("rssChannel");
        postFile = botProperties.getProperty("postFile");
        rssUrl = botProperties.getProperty("rssUrl");
    }
}

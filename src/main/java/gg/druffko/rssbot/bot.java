package gg.druffko.rssbot;

import gg.druffko.rssbot.config.Settings;
import gg.druffko.rssbot.events.InteractionEventListener;
import gg.druffko.rssbot.events.MessageEventListener;
import gg.druffko.rssbot.events.ReadyEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.List;

public class bot {
    public static JDA jda;

    public static void main(String[]args)throws LoginException, InterruptedException{
        JDABuilder jdabuilder = JDABuilder.createDefault(Settings.discordToken);

        jda = jdabuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new ReadyEventListener(), new InteractionEventListener())
                .build();
        jda.awaitReady();

        //Get Channel Name from ID
        TextChannel textChannel = jda.getTextChannelById(Settings.rssChannel);
        System.out.println("Posting into " + textChannel.getName());

        //Start RSS Grabber
        //RssGrabber.initializeRss();
    }
}

package gg.druffko.rssbot.events;

import gg.druffko.rssbot.config.Settings;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InteractionEventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        //event.reply("Hello").queue();

        switch (event.getName()){
            case "bot-status":
                event.reply("Hi, I'm Druffko's RSS Discord Bot and I am running! You can find me at: https://github.com/druffko-gg/discord-rss-bot").queue();
                break;
            case "rss-url":
                event.reply("I'm currently posting " + Settings.rssUrl + " into a channel on this server.").queue();
        }
    }
}

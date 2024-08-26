package gg.druffko.rssbot.content;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import gg.druffko.rssbot.config.Settings;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static gg.druffko.rssbot.bot.jda;

public class RssGrabber {

    public static ArrayList<String> postFileList = new ArrayList<String>();
    public static ArrayList<String> postRssList = new ArrayList<String>();

    public static String messageToSend;

    public static void initializeRSS(){
        checkForFile();
        readPostFile();
        grabRss();
        compareLists();
        checkForUpdates();
    }

    public static void checkForFile(){
        File postFile = new File(Settings.postFile);
        if (!postFile.exists()){
            try {
                postFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void readPostFile(){
        try {
            Scanner scanner = new Scanner(new File(Settings.postFile));
            while(scanner.hasNextLine()){
                postFileList.add(scanner.nextLine());
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void grabRss(){
        try {
            // Feed URL
            String feedUrl = Settings.rssUrl;

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(feedUrl)));

            List<SyndEntry> entries = feed.getEntries();

            for (SyndEntry entry : entries) {
                postRssList.add(entry.getTitle() + ": " + entry.getLink());
            }
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void compareLists(){
        int rssListSize = postRssList.size();
        for (int i = 0; i < rssListSize; i++){
            String rss = postRssList.get(i);
            if (postFileList.contains(rss)){
                //do nothing
            }else {
                //add to postlist, add to file, send message
                postFileList.add(0, postRssList.get(i));
                messageToSend = postRssList.get(i);
                //write result to file
                writeResultToFile(messageToSend);
                sendMessageToChannel();
            }

        }
    }

    public static void sendMessageToChannel(){
        TextChannel textChannel = jda.getTextChannelById(Settings.rssChannel);
        textChannel.sendMessage(messageToSend).queue();
    }

    public static void writeResultToFile(String resultToWrite){
        //write results
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Settings.postFile, true));
            writer.append("\n");
            writer.append(resultToWrite);

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkForUpdates(){
        int i = 1;
        //check for updates and post if required for as long as possible
        Thread checkForUpdatesTread = new Thread(() -> {
            //do forever
            while (true){
                try {
                    System.out.println("RSS Thread started");
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                    readPostFile();
                    grabRss();
                    compareLists();
                    System.out.println("RSS Thread finished");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        checkForUpdatesTread.start();
    }
}

package com.mattmerr.dr9k;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    private static HashSet<String> previous = new HashSet<>();
    private static HashMap<User, Punishment> punishments = new HashMap<>();

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder();
        String token = "NjE1Mzg2ODgwMTI4NzEyNzE0.XWNhLQ.Pg0THSi5oMEEXqrtb3Q4b8AVY-M";
        builder.setToken(token);
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot() || !event.getMessage().getChannelType().equals(ChannelType.TEXT)){
            return;
        }
        User user = event.getAuthor();
        System.out.print("We received a message");
        System.out.printf(" from %s: %s\n", user.getName(), event.getMessage().getContentDisplay());
        if(userIsBeingPunished(user)){
            Punishment userPunishment = getPunishment(user);
            System.out.println("Punished user detected: " + user.getName());
            if(userPunishment.getIsOver()){
                System.out.println("User is not currently muted.");
                if(userPunishment.getPunishmentDecayed()){
                    unpunishUser(user);
                }
            } else {
                System.out.println("Removing message from user.");
                event.getMessage().delete().queue();
                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                    String.format("You are currently muted in this channel. Please wait until your mute ends in %s to try again.", getPunishment(user).getHumanTimeRemaining())).queue());
                return;
            }
        }
        if(seenMessage(event.getMessage().getContentRaw())) {
            System.out.println("Muting " + user.getName());
            event.getMessage().delete().queue();
            if(userIsBeingPunished(event.getAuthor())){
                Punishment userPunishment = getPunishment(user);
                int decay = userPunishment.getPunishmentDecay();

                punishUser(user, userPunishment.getSeverityLevel()-decay+2);

                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                        String.format("You have been muted in this channel for repeating %s. This mute will last %s",
                                event.getMessage().getContentRaw(),
                                getPunishment(user).getHumanTimeRemaining())).queue());
            } else {
                punishUser(user, 1);
                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                        String.format("You have been muted in this channel for repeating %s. This mute will last 2 seconds.",
                                event.getMessage().getContentRaw())).queue());
            }
        }
    }

    private boolean seenMessage(String message){
        return !previous.add(message);
    }

    private boolean userIsBeingPunished(User user){
        return punishments.containsKey(user);
    }

    private void punishUser(User user, int severity){
        Punishment p = new Punishment(severity, Instant.now());
        System.out.println(p);
        punishments.put(user,p);
    }

    private Punishment getPunishment(User user){
        if(punishments.containsKey(user)){
            return punishments.get(user);
        }
        return null;
    }

    private void unpunishUser(User user){
        System.out.println("Removing " + user.getName());
        punishments.remove(user);
    }
}
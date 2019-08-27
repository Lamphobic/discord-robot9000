package com.mattmerr.dr9k;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

import com.mattmerr.dr9k.model.Message;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    private static HashSet<String> previous = new HashSet<>();
    private static HashMap<Long, Punishment> punishments = new HashMap<>();

    public static void main(String[] args) throws LoginException {
        String token = "NjE1Mzg2ODgwMTI4NzEyNzE0.XWSN2g.MI9PA8PYhxHI4UBe0Uo4JwF9U4k";
        JDABuilder builder = new JDABuilder()
                .setToken(token)
                .addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot() || !event.getMessage().getChannelType().equals(ChannelType.TEXT)){
            return;
        }
        User user = event.getAuthor();
        //Message message = event.getMessage(); //TODO: make an issue about out message class.
        String location = String.format("Guild Server: %s, Channel: %s", event.getMessage().getGuild().getName(), event.getMessage().getChannel().getName());
        System.out.print("We received a message");
        System.out.printf(" from %s: %s\n", user.getName(), event.getMessage().getContentDisplay());
        if(userIsBeingPunished(user)){
            Punishment userPunishment = getPunishment(user);
            System.out.println("Punished user detected: " + user.getName());
            if(userPunishment.isOver()){
                System.out.println("User is not currently muted.");
                if(userPunishment.getPunishmentDecayed()){
                    unpunishUser(user);
                }
            } else {
                System.out.println("Removing message from user.");
                event.getMessage().delete().queue();
                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                    String.format("You are currently muted in %s. Please wait until your mute ends in %s to try again.", location, getPunishment(user).getHumanTimeRemaining())).queue());
                return;
            }
        }
        if(seenMessage(event.getMessage().getContentRaw())) {
            System.out.println("Muting " + user.getName());
            event.getMessage().delete().queue();
            if(userIsBeingPunished(user)){
                Punishment userPunishment = getPunishment(user);
                int decay = userPunishment.getPunishmentDecay();

                punishUser(user, userPunishment.getSeverityLevel()-decay+2);
                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                        String.format("You have been muted in %s for repeating %s. This mute will last %s",
                                location,
                                event.getMessage().getContentRaw(),
                                getPunishment(user).getHumanTimeRemaining())).queue());
            } else {
                punishUser(user, 1);
                user.openPrivateChannel().queue((channel) -> channel.sendMessage(
                        String.format("You have been muted in %s for repeating %s. This mute will last 2 seconds.",
                                location,
                                event.getMessage().getContentRaw())).queue());
            }
        }
    }

    private boolean seenMessage(String message){
        return !previous.add(message);
    }

    private boolean userIsBeingPunished(User user){
        return punishments.containsKey(user.getIdLong());
    }

    private void punishUser(User user, int severity){
        Punishment p = new Punishment(severity, Instant.now());
        System.out.println(p);
        punishments.put(user.getIdLong(),p);
    }

    private Punishment getPunishment(User user){
        if(punishments.containsKey(user)){
            return punishments.get(user.getIdLong());
        }
        return null;
    }

    private void unpunishUser(User user){
        System.out.println("Removing " + user.getName());
        punishments.remove(user);
    }
}
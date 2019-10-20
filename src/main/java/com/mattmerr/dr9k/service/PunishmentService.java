package com.mattmerr.dr9k.service;

import com.google.inject.Inject;
import com.mattmerr.dr9k.model.Punishment;
import io.ebean.Database;
import java.time.Instant;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PunishmentService {
  private static final int decaySeconds = 30; // 21600 for 6 hours

  private static final Logger logger = LoggerFactory.getLogger(PunishmentService.class);

  private final Database db;

  @Contract(pure = true)
  public static int getDecaySeconds() {
    return decaySeconds;
  }

  @Contract(pure = true)
  @Inject
  public PunishmentService(Database db) {
    this.db = db;
  }

  public void insertPunishment(Message message, int punishmentLevel) {
    try {
      var entry =
          new Punishment()
              .setGuildId(message.getGuild().getId())
              .setAuthorId(message.getAuthor().getId())
              .setSeverityLevel(punishmentLevel)
              .setPunishmentStart(Instant.now());
      db.insert(entry);
    } catch (Exception exception) {
      logger.error("Error inserting punishment", exception);
    }
  }

  public boolean userHasPunishmentRecord(@NotNull Message message) {
    return db.createQuery(Punishment.class)
        .where()
        .eq("guildId", message.getGuild().getId())
        .eq("authorId", message.getAuthor().getId())
        .exists();
  }

  public Punishment getPunishment(@NotNull Message message) {
    return db.createQuery(Punishment.class)
        .where()
        .eq("guildId", message.getGuild().getId())
        .eq("authorId", message.getAuthor().getId())
        .findOne();
  }

  public int removePunishment(@NotNull Message message) {
    return db.createQuery(Punishment.class)
        .where()
        .eq("guildId", message.getGuild().getId())
        .eq("authorId", message.getAuthor().getId())
        .delete();
  }

  public boolean handlePunishment(Message message) {
    Punishment userPunishment = getPunishment(message);
    System.out.println("Punished user detected: " + message.getAuthor().getName());
    if (userPunishment.isOver()) {
      System.out.println("User is not currently muted.");
      if (userPunishment.getPunishmentDecayed()) {
        removePunishmentRecord(message);
      }
    } else {
      enforcePunishment(message);
      return true;
    }
    return false;
  }

  public void enforcePunishment(Message message) {
    String location =
        String.format(
            "Guild Server: %s, Channel: %s",
            message.getGuild().getName(), message.getChannel().getName());
    System.out.println("Removing message from user.");
    message.delete().queue();
    message
        .getAuthor()
        .openPrivateChannel()
        .queue(
            (channel) ->
                channel
                    .sendMessage(
                        String.format(
                            "You are currently muted in %s. Please wait until your mute "
                                + "ends in %s to try again.",
                            location, getPunishment(message).getHumanTimeRemaining()))
                    .queue());
  }

  public void punish(Message message) {
    String location =
        String.format(
            "Guild Server: %s, Channel: %s",
            message.getGuild().getName(), message.getChannel().getName());
    System.out.println("Deleting message");
    message.delete().queue();
    if (userHasPunishmentRecord(message)) {
      Punishment userPunishment = getPunishment(message);
      int decay = userPunishment.getPunishmentDecay();
      punishUser(message, userPunishment.getSeverityLevel() - decay + 2);
      message
          .getAuthor()
          .openPrivateChannel()
          .queue(
              (channel) ->
                  channel
                      .sendMessage(
                          String.format(
                              "You have been muted in %s for repeating %s. This mute will "
                                  + "last %s",
                              location,
                              message.getContentRaw(),
                              getPunishment(message).getHumanTimeRemaining()))
                      .queue());
    } else {
      punishUser(message, 1);
      message
          .getAuthor()
          .openPrivateChannel()
          .queue(
              (channel) ->
                  channel
                      .sendMessage(
                          String.format(
                              "You have been muted in %s for repeating %s. This mute will "
                                  + "last 2 seconds.",
                              location, message.getContentRaw()))
                      .queue());
    }
  }

  private void removePunishmentRecord(Message message) {
    System.out.println("Removing record " + message.getAuthor().getName());
    removePunishment(message);
  }

  private void punishUser(Message message, int severity) {
    if (userHasPunishmentRecord(message)) {
      removePunishmentRecord(message);
    }
    String location =
        String.format(
            "Guild Server: %s, Channel: %s",
            message.getGuild().getName(), message.getChannel().getName());
    System.out.printf(
        "Punishing %s in %s at severity %d\n", message.getAuthor().getName(), location, severity);
    insertPunishment(message, severity);
  }
}

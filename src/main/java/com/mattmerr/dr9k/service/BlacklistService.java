package com.mattmerr.dr9k.service;

import com.mattmerr.dr9k.model.BlacklistEntry;

import com.google.inject.Inject;
import io.ebean.Database;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class BlacklistService {

  private static final Logger logger =
      LoggerFactory.getLogger(BlacklistService.class);

  private final Database db;

  @Inject
  public BlacklistService(Database db) {
    this.db = db;
  }

  // TODO: Return true/false depending if message already existed
  public void insertMessage(Message message) {
    try {
      var entry = new BlacklistEntry().setGuildId(message.getGuild().getId())
          .setChannelId(message.getChannel().getId())
          .setAuthorId(message.getAuthor().getId())
          .setMessageId(message.getId());
      db.insert(entry);
    } catch (Exception exception) {
      logger.error("Error inserting message", exception);
    }
  }

  public boolean violatesUserUniqueness(Message message) {
    return db.createQuery(BlacklistEntry.class).where()
        .eq("contents", message.getContentStripped())
        .eq("userId", message.getAuthor().getId())
        .eq("guildId", message.getGuild().getId()).exists();
  }

  public boolean violatesChannelUniqueness(Message message) {
    return db.createQuery(BlacklistEntry.class).where()
        .eq("contents", message.getContentStripped())
        .eq("channelId", message.getChannel().getId())
        .eq("guildId", message.getGuild().getId()).exists();
  }

  public boolean violatesChannelUniqueness(
      Message message, Collection<String> channels) {
    return db.createQuery(BlacklistEntry.class).where()
        .eq("contents", message.getContentStripped())
        .isIn("channelId", channels)
        .eq("guildId", message.getGuild().getId())
        .exists();
  }

  public boolean violatesGuildUniqueness(Message message) {
    return db.createQuery(BlacklistEntry.class).where()
        .eq("contents", message.getContentStripped())
        .eq("guildId", message.getGuild().getId()).exists();
  }
}

package com.mattmerr.dr9k.service;

import com.mattmerr.dr9k.BotConfiguration;
import com.mattmerr.dr9k.model.BlacklistEntry;

import com.google.inject.Inject;
import io.ebean.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlacklistService {

  private static final Logger logger = LoggerFactory.getLogger(BlacklistService.class);

  private final Database db;

  @Inject
  public BlacklistService(Database db) {
    this.db = db;
  }

  public boolean insertMessage(String domain, String content, String discordId) {
    try {
      var entry = new BlacklistEntry()
          .setDomain(domain)
          .setContents(content)
          .setDiscordId(discordId);
      db.insert(entry);
    }
    catch (Exception exception) {
      logger.error("Error inserting message", exception);
    }
    return false;
  }

}

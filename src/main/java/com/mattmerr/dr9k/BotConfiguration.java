package com.mattmerr.dr9k;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomlj.Toml;

import java.io.IOException;
import java.nio.file.Path;

public class BotConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

  private String discordToken = null;
  private String discordBotName = null;

  BotConfiguration loadFromToml(Path path) {
    try {
      var toml = Toml.parse(path);
      if (toml.hasErrors()) {
        logger.error("Error(s) parsing TOML at {}", path.toString());
        for (var tomlError : toml.errors()) {
          logger.error(tomlError.toString());
        }
      }
      else {
        if (discordToken == null) {
          discordToken = toml.getString("discord.token");
        }
        if (discordBotName == null) {
          discordBotName = toml.getString("discord.botName");
        }
      }
    }
    catch (IOException ioException) {
      logger.error("Error reading TOML configuration file", ioException);
    }
    return this;
  }

  public String getDiscordToken() {
    return discordToken;
  }

  public String getDiscordBotName() {
    return discordBotName;
  }

}

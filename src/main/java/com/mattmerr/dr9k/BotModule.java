package com.mattmerr.dr9k;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import java.nio.file.Path;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotModule extends AbstractModule {

  private static final Logger logger = LoggerFactory.getLogger(BotModule.class);

  @Override
  protected void configure() {
    BotConfiguration configuration =
        new BotConfiguration()
            .loadFromToml(Path.of("config-secret.toml"))
            .loadFromToml(Path.of("config.toml"));
    bind(BotConfiguration.class).toInstance(configuration);
  }

  @Inject
  @Provides
  private JDA provideJDA(BotConfiguration configuration) {
    try {
      // TODO: Configurable activity
      return new JDABuilder().setToken(configuration.getDiscordToken()).build();
    } catch (LoginException loginException) {
      logger.error("Could not construct JDA", loginException);
      System.exit(1);
      return null;
    }
  }

  @Inject
  @Provides
  private Database provideDatabase() {
    try {
      var dbConf = new DatabaseConfig();
      dbConf.loadFromProperties();
      return DatabaseFactory.create(dbConf);
    } catch (Exception e) {
      logger.error("Could not construct Database", e);
      System.exit(1);
      return null;
    }
  }
}

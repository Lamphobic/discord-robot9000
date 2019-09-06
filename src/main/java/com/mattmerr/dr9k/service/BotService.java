package com.mattmerr.dr9k.service;

import com.mattmerr.dr9k.BotConfiguration;

import com.google.inject.Inject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotService extends ListenerAdapter {

  private static final Logger logger =
      LoggerFactory.getLogger(BotConfiguration.class);

  private final JDA jda;
  private final BlacklistService blacklistService;
  private final PunishmentService punishmentService;

  @Inject //TODO: ConfigureationService
  BotService(JDA jda, BlacklistService blacklistService,
             PunishmentService punishmentService) {
    this.jda = jda;
    this.blacklistService = blacklistService;
    this.punishmentService = punishmentService;

    this.jda.addEventListener(this);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        super.run();
        disconnect();
      }
    });
  }

  public void connect() throws InterruptedException {
    logger.info("Connecting...");
    jda.awaitReady();
    logger.info("Connected! :)");
  }

  public void disconnect() {
    if (jda.getStatus() != JDA.Status.DISCONNECTED) {
      logger.info("Disconnecting...");
      jda.shutdown();
      logger.info("Disconnected. :(");
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    User user = event.getAuthor();
    Message message = event.getMessage();
    if (event.getAuthor().isBot() || !event.getMessage().getChannelType()
        .equals(ChannelType.TEXT)) {
      return;
    }
    System.out.print("We received a message");
    System.out.printf(" from %s: %s\n", user.getName(),
        event.getMessage().getContentDisplay());
    if(userHasPunishmentRecord(message)){
      boolean userStillMuted = punishmentService.handlePunishment(message);
      if(userStillMuted){
        return;
      }
    }
    if(messageDeservesPunishment(message)){
      punishmentService.punish(message);
    }
  }

  private boolean messageDeservesPunishment(Message message) {
    boolean response = blacklistService.violatesUserUniqueness(message);
    if(!response){
      blacklistService.insertMessage(message);
    }
    return response;
  }

  private boolean userHasPunishmentRecord(Message message) {
    return punishmentService.userHasPunishmentRecord(message);
  }

}

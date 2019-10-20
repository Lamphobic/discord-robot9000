package com.mattmerr.dr9k;

import com.google.inject.Guice;
import com.mattmerr.dr9k.service.BotService;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    var injector = Guice.createInjector(new BotModule());
    var botService = injector.getInstance(BotService.class);
    botService.connect();
  }
}

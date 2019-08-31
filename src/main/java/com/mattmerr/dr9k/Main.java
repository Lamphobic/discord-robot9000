package com.mattmerr.dr9k;

import com.mattmerr.dr9k.service.BotService;

import com.google.inject.Guice;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    var injector = Guice.createInjector(new BotModule());
    var botService = injector.getInstance(BotService.class);
    botService.connect();
  }
}

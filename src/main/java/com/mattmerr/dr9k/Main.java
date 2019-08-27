package com.mattmerr.dr9k;

import com.mattmerr.dr9k.service.BotService;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Injector injector = Guice.createInjector(new BotModule());
        BotService botService = injector.getInstance(BotService.class);
        botService.connect();
    }
}
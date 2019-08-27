package com.mattmerr.dr9k;

import com.google.inject.Inject;
import io.ebean.DB;

public class MessageLogService {

  @Inject
  public MessageLogService() {

  }

  public boolean insertMessage(String domain, String content, String discordId) {
    var txn = DB.beginTransaction();
//    Message msg = new Message();
//    msg = domain;
//    msg.content = content;
    return false;
  }

}

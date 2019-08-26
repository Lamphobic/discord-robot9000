package com.mattmerr.dr9k.model;

import com.mattmerr.dr9k.model.Message;

import io.ebean.DB;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class MessageTest {

  @Test
  public void insertFindDelete() {
    Message message = new Message();
    message.contents = "foo";
    message.userId = "user";

    DB.save(message);

    Message found = DB.find(Message.class, "foo");
    assertThat(found).isNotNull();
    assertThat(found.userId).isEqualTo("user");

    assertThat(DB.delete(message)).isTrue();
    assertThat(DB.delete(message)).isFalse();
  }
}

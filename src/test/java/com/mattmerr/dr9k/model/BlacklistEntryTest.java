package com.mattmerr.dr9k.model;

import io.ebean.DB;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class BlacklistEntryTest {

  @Test
  public void insertFindDelete() {
    BlacklistEntry entry = new BlacklistEntry();
    entry.setDomain("test_domain");
    entry.setContents("foo");
    entry.setDiscordId("DISCORD_ID");

    DB.save(entry);

    BlacklistEntry found = DB.createQuery(BlacklistEntry.class)
        .where()
        .eq("domain", "test_domain")
        .eq("contents", "foo")
        .findOne();

    assertThat(found).isNotNull();
    assertThat(found.getDomain()).isEqualTo("test_domain");
    assertThat(found.getContents()).isEqualTo("foo");
    assertThat(found.getDiscordId()).isEqualTo("DISCORD_ID");

    assertThat(DB.delete(entry)).isTrue();
    assertThat(DB.delete(entry)).isFalse();
  }
}

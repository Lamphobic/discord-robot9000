package com.mattmerr.dr9k.model;

import io.ebean.Model;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist_entries")
public class BlacklistEntry extends Model {

  @Id
  @Length(512)
  private String contents;

  @NotNull
  @Length(512)
  private String domain;

  @NotNull
  @Length(512)
  private String discordId;

  public BlacklistEntry() {
  }

  public String getDomain() {
    return domain;
  }
  public BlacklistEntry setDomain(String domain) {
    this.domain = domain;
    return this;
  }

  public String getContents() {
    return contents;
  }
  public BlacklistEntry setContents(String contents) {
    this.contents = contents;
    return this;
  }

  public String getDiscordId() {
    return discordId;
  }
  public BlacklistEntry setDiscordId(String discordId) {
    this.discordId = discordId;
    return this;
  }
}

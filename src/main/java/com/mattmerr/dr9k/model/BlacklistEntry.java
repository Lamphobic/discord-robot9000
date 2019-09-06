package com.mattmerr.dr9k.model;

import io.ebean.Model;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "blacklist_entries")
public class BlacklistEntry extends Model {
  private final int decaySeconds = 30; //21600 for 6 hours

  @Id
  private Long id;

  @NotNull
  @Length(512)
  private String contents;

  @NotNull
  @Length(512)
  private String guildId;

  @NotNull
  @Length(512)
  private String channelId;

  @NotNull
  @Length(512)
  private String authorId;

  @NotNull
  @Length(512)
  private String messageId;

  public Long getId() {
    return id;
  }

  public BlacklistEntry setId(Long id) {
    this.id = id;
    return this;
  }

  public String getContents() {
    return contents;
  }

  public BlacklistEntry setContents(String contents) {
    this.contents = contents;
    return this;
  }

  public String getGuildId() {
    return guildId;
  }

  public BlacklistEntry setGuildId(String guildId) {
    this.guildId = guildId;
    return this;
  }

  public String getChannelId() {
    return channelId;
  }

  public BlacklistEntry setChannelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  public String getMessageId() {
    return messageId;
  }

  public BlacklistEntry setMessageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  public String getAuthorId() {
    return authorId;
  }

  public BlacklistEntry setAuthorId(String authorId) {
    this.authorId = authorId;
    return this;
  }
}

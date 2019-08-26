package com.mattmerr.dr9k.model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message extends Model {

  @Id
  String contents;

  String userId;

}

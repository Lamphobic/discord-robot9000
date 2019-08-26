package com.mattmerr.dr9k.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {

  @Id
  String contents;

}

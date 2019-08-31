create table blacklist_entries (
  id                            bigint auto_increment not null,
  contents                      varchar(512),
  domain                        varchar(512) not null,
  discord_id                    varchar(512) not null,
  constraint uq_blacklist_entries_contents_domain unique (contents,domain),
  constraint pk_blacklist_entries primary key (id)
);


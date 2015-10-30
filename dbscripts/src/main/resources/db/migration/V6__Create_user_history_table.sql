create table userloginhistory (
  id bigint primary key not null auto_increment,
  userid bigint not null references user(id),
  platform varchar(255) not null,
  tokenid varchar(255) not null,
  arnendpoint varchar(255) default null,
  createdon datetime not null,
  updatedon datetime
)engine=innodb;
create table otprequestcount (
  id bigint primary key not null auto_increment,
  otptype varchar(255) not null,
  otpsource varchar(255) not null,
  requestcount int(11) not null,
  userid bigint not null references user(id),
  createdon datetime not null,
  updatedon datetime default null
  )engine=innodb;
create table user (
    id bigint primary key not null auto_increment,
    username varchar(255),
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    email varchar(255) not null,
    passwordsalt varchar(255),
    passwordhash varchar(255),
    lastlogin datetime,
    initials varchar(64),
    gender varchar(10),
    active bool,
    emailConfirmed bool default false,
    phonenumber varchar(255),
    addressline1 varchar(255),
    addressline2 varchar(255),
    city varchar(255),
    zippostalcode varchar(255),
    state varchar(255),
    creationsource varchar(255) not null,
    password varchar(255) not null,
    createdon datetime not null,
    updatedon datetime
) engine=innodb;

alter table user add index (username);
alter table user add index (email);

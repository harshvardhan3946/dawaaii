create table role (
    id bigint primary key not null auto_increment,
    name varchar(255) not null,
    code varchar(255) not null,
    createdon datetime not null,
    updatedon datetime
) engine=innodb;

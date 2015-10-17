create table role_permission (
    id bigint primary key not null auto_increment,
    role_id bigint not null references role(id),
    permission varchar(255) not null
) engine=innodb;

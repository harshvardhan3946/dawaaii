create table role_permission (
    id bigint primary key not null auto_increment,
    iconnect_role_id bigint not null references iconnect_role(id),
    iconnect_permission varchar(255) not null
) engine=innodb;

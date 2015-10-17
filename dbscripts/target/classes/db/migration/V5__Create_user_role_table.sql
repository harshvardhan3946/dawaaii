create table user_iconnect_role (
    id bigint primary key not null auto_increment,
    roles_id bigint not null references iconnect_role(id),
    user_id bigint not null references user(id)
) engine=innodb;

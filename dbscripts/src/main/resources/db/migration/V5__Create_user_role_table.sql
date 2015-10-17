create table user_role (
    id bigint primary key not null auto_increment,
    roles_id bigint not null references role(id),
    user_id bigint not null references user(id)
) engine=innodb;

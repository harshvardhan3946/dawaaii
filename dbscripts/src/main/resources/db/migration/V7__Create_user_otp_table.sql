create table userotp (
    id bigint primary key not null auto_increment,
    userid bigint not null references user(id),
    otp varchar(10),
    otptype varchar(255) not null,
    otpsource varchar(255) not null,
    uuid varchar(255) not null,
    expireson datetime,
    used bool,
    usedon datetime,
    createdon datetime not null,
    updatedon datetime
) engine=innodb;
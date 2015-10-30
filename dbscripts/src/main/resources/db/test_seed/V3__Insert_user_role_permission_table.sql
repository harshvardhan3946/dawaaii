-- Insert roles
insert into role(name, code, createdon)
values("System Administrator", "SYSTEM_ADMINISTRATOR", CURRENT_TIMESTAMP);
insert into role(name, code, createdon)
values("Admin Portal User", "ADMIN_PORTAL_USER", CURRENT_TIMESTAMP);
insert into role(name, code, createdon)
values("WEBSITE_ROLE_USER", "WEBSITE_APP_GENERAL_USER", CURRENT_TIMESTAMP);
insert into role(name, code, createdon)
values("CUSTOMER_SUPPORT", "CUSTOMER_SUPPORT", CURRENT_TIMESTAMP);

-- Insert permissions to role
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "ADMIN_PORTAL_LOGIN");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "USER_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "TRIP_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "DEVICE_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "VEHICLE_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "EVENT_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "PRODUCT_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "ORDER_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "MONITOR_APPS");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "CUSTOMER_SUPPORT_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="SYSTEM_ADMINISTRATOR"), "PROMO_CODE_MANAGEMENT");
insert into role_permission(role_id, permission)
values((select id from role where code="ADMIN_PORTAL_USER"), "ADMIN_PORTAL_LOGIN");
insert into role_permission(role_id, permission)
values((select id from role where code="CUSTOMER_SUPPORT"), "CUSTOMER_SUPPORT_MANAGEMENT");
-- Associate user to roles
insert into user_role(user_id, roles_id)
values((select id from user where email="rohit.mishra0411@gmail.com"), (select id from role where code="WEBSITE_APP_GENERAL_USER"));
insert into user_role(user_id, roles_id)
values((select id from user where email="ojha.harshvardhan@gmail.com"), (select id from role where code="SYSTEM_ADMINISTRATOR"));

commit;
-- admin portal user

insert into user(firstname, lastname, email, passwordsalt, passwordhash, active, emailConfirmed, creationsource, password, createdon)
values("harshvardhan","ojha","ojha.harshvardhan@gmail.com", "[B@74646ad7", "eryGe08TIipM1diif9c7WjLEisngaTuc2tja1iizeNooipiskisHk7zRiiQUlsFXk0w9NBwRICK5Z3qSfBvk8istxcVCaWAieie", true, true, "ADMIN_PORTAL", "fe01ce2a7fbac8fafaed7c982a04e229", CURRENT_TIMESTAMP);

insert into user(firstname, lastname, email, passwordsalt, passwordhash, active, emailConfirmed, creationsource, password, createdon)
values("rohit","mishra","rohit.mishra0411@gmail.com", "[B@74646ad7", "eryGe08TIipM1diif9c7WjLEisngaTuc2tja1iizeNooipiskisHk7zRiiQUlsFXk0w9NBwRICK5Z3qSfBvk8istxcVCaWAieie", true, true, "ADMIN_PORTAL", "fe01ce2a7fbac8fafaed7c982a04e229", CURRENT_TIMESTAMP);

commit;
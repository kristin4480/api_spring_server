create table if not exists `role` (id int(11) not null primary key auto_increment, `name` varchar(255) not null);
insert into `role` (`name`) values ("ROLE_ADMIN"),("ROLE_USER");
alter table user add `role_id` int(11) after `id`;
alter table user add foreign key (`role_id`) references `role`(`id`) on delete restrict on update restrict;

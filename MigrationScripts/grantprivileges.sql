-- create database website charset=utf8;
-- create database zgprogram charset=utf8;
-- mysql --default-character-set=gbk -uroot -p
--mysql --default-character-set=gbk -uroot -p <website.sql
--mysql --default-character-set=gbk -uroot -p <zgprogram.sql

grant all privileges on website.* to zhanggan@localhost identified by 'zhanggan';
grant all privileges on zgprogram.* to zhanggan@localhost identified by 'zhanggan';
-- grant all privileges on website.* to zhanggan@'%' identified by 'zhanggan';
flush privileges;

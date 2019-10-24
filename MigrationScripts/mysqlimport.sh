mysql -uroot --default-character-set=utf8
create database website;
create database zgprogram;
mysql -uroot --default-character-set=utf8 website <website.sql
mysql -uroot --default-character-set=utf8 zgprogram<zgprogram.sql
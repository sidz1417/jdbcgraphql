drop table if exists product;

create table if not exists product
(
    id serial primary key,
    name text,
    category text,
    stock int,
    price int
);
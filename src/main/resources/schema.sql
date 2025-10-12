drop table if exists product;

create table if not exists product
(
    id serial primary key,
    name text,
    category text,
    stock int,
    price int
);

-- Electronics Category
INSERT INTO product (name, category, price, stock) VALUES ('iPhone 14 Pro', 'Electronics', 999, 50);
INSERT INTO product (name, category, price, stock) VALUES ('Samsung Galaxy S23', 'Electronics', 899, 45);
INSERT INTO product (name, category, price, stock) VALUES ('MacBook Pro M2', 'Electronics', 1499, 30);
INSERT INTO product (name, category, price, stock) VALUES ('Dell XPS 15', 'Electronics', 1299, 25);
INSERT INTO product (name, category, price, stock) VALUES ('AirPods Pro', 'Electronics', 249, 100);
INSERT INTO product (name, category, price, stock) VALUES ('Sony WH-1000XM4', 'Electronics', 349, 40);
INSERT INTO product (name, category, price, stock) VALUES ('iPad Air', 'Electronics', 599, 60);
INSERT INTO product (name, category, price, stock) VALUES ('Nintendo Switch', 'Electronics', 299, 75);

-- Books Category
INSERT INTO product (name, category, price, stock) VALUES ('The Midnight Library', 'Books', 15, 200);
INSERT INTO product (name, category, price, stock) VALUES ('Atomic Habits', 'Books', 20, 150);
INSERT INTO product (name, category, price, stock) VALUES ('Project Hail Mary', 'Books', 18, 120);
INSERT INTO product (name, category, price, stock) VALUES ('The Psychology of Money', 'Books', 17, 180);
INSERT INTO product (name, category, price, stock) VALUES ('Spring Boot in Action', 'Books', 45, 50);
INSERT INTO product (name, category, price, stock) VALUES ('Clean Code', 'Books', 35, 45);

-- Home & Kitchen
INSERT INTO product (name, category, price, stock) VALUES ('KitchenAid Mixer', 'Home & Kitchen', 299, 30);
INSERT INTO product (name, category, price, stock) VALUES ('Ninja Air Fryer', 'Home & Kitchen', 119, 65);
INSERT INTO product (name, category, price, stock) VALUES ('Instant Pot', 'Home & Kitchen', 89, 80);
INSERT INTO product (name, category, price, stock) VALUES ('Dyson V15', 'Home & Kitchen', 699, 25);
INSERT INTO product (name, category, price, stock) VALUES ('Coffee Maker', 'Home & Kitchen', 79, 90);
INSERT INTO product (name, category, price, stock) VALUES ('Robot Vacuum', 'Home & Kitchen', 249, 40);

-- Sports & Outdoors
INSERT INTO product (name, category, price, stock) VALUES ('Yoga Mat', 'Sports', 29, 150);
INSERT INTO product (name, category, price, stock) VALUES ('Dumbbells Set', 'Sports', 149, 35);
INSERT INTO product (name, category, price, stock) VALUES ('Tennis Racket', 'Sports', 89, 45);
INSERT INTO product (name, category, price, stock) VALUES ('Basketball', 'Sports', 25, 100);
INSERT INTO product (name, category, price, stock) VALUES ('Running Shoes', 'Sports', 129, 75);

-- Fashion
INSERT INTO product (name, category, price, stock) VALUES ('Leather Wallet', 'Fashion', 49, 100);
INSERT INTO product (name, category, price, stock) VALUES ('Sunglasses', 'Fashion', 159, 60);
INSERT INTO product (name, category, price, stock) VALUES ('Watch', 'Fashion', 199, 40);
INSERT INTO product (name, category, price, stock) VALUES ('Backpack', 'Fashion', 79, 85);
INSERT INTO product (name, category, price, stock) VALUES ('Winter Jacket', 'Fashion', 189, 50);

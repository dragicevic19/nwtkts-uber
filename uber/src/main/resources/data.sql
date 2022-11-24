INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Dunavska 2');

-- lozinka je 123
INSERT INTO users (blocked, email, enabled, first_name, last_name, last_password_reset_date, password, phone_number, photo, address_id)
VALUES (false, 'test@gmail.com', true, 'Pera', 'Peric', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0665011426', 'slika.jpg', 1);

insert into admin
values (1);

insert into role (name)
values ('ROLE_ADMIN');
insert into role (name)
values ('ROLE_CLIENT');
insert into role (name)
values ('ROLE_DRIVER');

insert into user_role
values (1, 1);

insert into vehicle_type (name, price_per_km) values ('HATCHBACK', 1);
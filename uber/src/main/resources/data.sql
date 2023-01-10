-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Dunavska 2');
-- lozinka je 123
INSERT INTO users (blocked, email, enabled, first_name, last_name, last_password_reset_date, password, phone_number,
                   photo, address_id)
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

insert into vehicle_type (name, price_per_km)
values ('HATCHBACK', 1);

-- first driver
insert into vehicle_type (name, price_per_km)
values ('SEDAN', 1.3);
insert into rating (average, num_of_votes)
values (4.5, 2);
insert into rating (average, num_of_votes)
values (4, 1);
insert into vehicle (babies_allowed, license_plate_number, make, make_year, model, pets_allowed, rating_id, type_id)
values (true, 'NS-128-FR', 'BMW', 2018, '3 Series', true, 1, 2);
INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Bele njive 24');
insert into users (blocked, email, enabled, first_name, full_reg_done, last_name, last_password_reset_date, password,
                  phone_number, photo, address_id)
values (false, 'driver@gmail.com', true, 'Vozac', true, 'Vozacevic', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06650552352',
        'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        2);
insert into driver (active, available, id, rating_id, vehicle_id)
values (true, true, 2, 2, 1);
insert into user_role
values (2, 3);

-- second driver
insert into rating (average, num_of_votes)
values (5, 2);
insert into rating (average, num_of_votes)
values (4.5, 2);
insert into vehicle (babies_allowed, license_plate_number, make, make_year, model, pets_allowed, rating_id, type_id)
values (true, 'NS-222-FR', 'BMW', 2018, '3 Series', true, 3, 2);
INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Bele njive 29');
insert into users (blocked, email, enabled, first_name, full_reg_done, last_name, last_password_reset_date, password,
                   phone_number, photo, address_id)
values (false, 'seconddriver@gmail.com', true, 'Drugi', true, 'Vozacevic', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06650552352',
        'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        3);
insert into driver (active, available, id, rating_id, vehicle_id)
values (false, false, 3, 4, 2);
insert into user_role
values (3, 3);
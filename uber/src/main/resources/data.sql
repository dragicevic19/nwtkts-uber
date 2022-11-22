INSERT INTO address
VALUES (1, 'Novi Sad', 'Serbia', 'Dunavska 2');

-- lozinka je 123
INSERT INTO users
VALUES (1, false, 'test@gmail.com', 'Pera', 'Peric', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0665011426', 'slika.jpg', 1);

insert into admin
values (1);

insert into role
values (1, 'ROLE_ADMIN');

insert into user_role
values (1, 1);
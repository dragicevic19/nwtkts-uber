-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Dunavska 2');
-- lozinka je 123
INSERT INTO users (blocked, email, enabled, first_name, full_reg_done, last_name, last_password_reset_date, password, phone_number,
                   photo, address_id, version)
VALUES (false, 'test@gmail.com', true, 'Pera', true, 'Peric', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0665011426', 'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56', 1, 0);
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

insert into vehicle_type (name, additional_price)
values ('HATCHBACK', 0);

-- first driver
insert into vehicle_type (name, additional_price)
values ('SEDAN', 1);
insert into rating (average, num_of_votes)
values (4.5, 2);
insert into rating (average, num_of_votes)
values (4, 1);
insert into vehicle (babies_allowed, license_plate_number, make, make_year, model, pets_allowed, rating_id, type_id, latitude, longitude)
values (false, 'NS-128-FR', 'BMW', 2018, '3 Series', false, 1, 2, 45.234150, 19.834890);
INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Bele njive 24');
insert into users (blocked, email, enabled, first_name, full_reg_done, last_name, last_password_reset_date, password,
                  phone_number, photo, address_id, version)
values (false, 'driver@gmail.com', true, 'Vozac', true, 'Vozacevic', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06650552352',
        'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        2, 0);
insert into driver (active, available, id, rating_id, vehicle_id)
values (false, false, 2, 2, 1);
insert into user_role
values (2, 3);

-- second driver
insert into rating (average, num_of_votes)
values (5, 2);
insert into rating (average, num_of_votes)
values (4.5, 2);
insert into vehicle (babies_allowed, license_plate_number, make, make_year, model, pets_allowed, rating_id, type_id, latitude, longitude)
values (true, 'NS-222-FR', 'BMW', 2018, '3 Series', true, 3, 2, 45.2609635910084, 19.843166366367612);
INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Bele njive 29');
insert into users (blocked, email, enabled, first_name, full_reg_done, last_name, last_password_reset_date, password,
                   phone_number, photo, address_id, version)
values (false, 'seconddriver@gmail.com', true, 'Drugi', true, 'Vozacevic', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06650552352',
        'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        3, 0);
insert into driver (active, available, id, rating_id, vehicle_id)
values (false, false, 3, 4, 2);
insert into user_role
values (3, 3);


-- inserti za klijente

------ BEGIN Klijent 1 ------

INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Dunavska 3');

-- lozinka je 123
INSERT INTO users (blocked, email, enabled, first_name, last_name, last_password_reset_date, password, phone_number,
                   photo, address_id, full_reg_done, version)
VALUES (false, 'user@gmail.com', true, 'Korisnik', 'Korisnic', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0661234567', 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp', 4, true, 0);

INSERT INTO client (id, verification_code, auth_provider, tokens)
VALUES (4, '', 'LOCAL', 50);

INSERT INTO user_role
VALUES (4, 2);

------ END Klijent 1 ------

------ BEGIN Klijent 2 ------

INSERT INTO address (city, country, street)
VALUES ('Novi Sad', 'Serbia', 'Dunavska 5');

-- lozinka je 123
INSERT INTO users (blocked, email, enabled, first_name, last_name, last_password_reset_date, password, phone_number,
                   photo, address_id, full_reg_done, version)
VALUES (false, 'seconduser@gmail.com', true, 'Korisnik2', 'Korisnic2', '2021-10-11 18:57:58.508-07',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0661234576', 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp', 5, true, 0);

INSERT INTO client (id, verification_code, auth_provider, tokens)
VALUES (5, '', 'LOCAL', 50);

INSERT INTO user_role
VALUES (5, 2);

------ END Klijent 2 ------

------ BEGIN inserti za voznje ------
-- voznja 1
INSERT INTO ride (babies_on_ride, calculated_duration, cancellation_reason, ending_latitude, ending_longitude, pets_on_ride, price, ride_status, routejson, scheduled_for, start_time, starting_latitude, starting_longitude, vehicle_id, driver_id, type_id)
VALUES (false, 10, '', 45.248564980392324, 19.848887767917674, false, 100, 'ENDED', '[{"steps":[{"geometry":{"coordinates":[[19.840628,45.248112],[19.840615,45.248137],[19.840608,45.24815],[19.840596,45.248175],[19.840568,45.24823]],"type":"LineString"},"maneuver":{"bearing_after":340,"bearing_before":0,"location":[19.840628,45.248112],"modifier":"right","type":"depart"},"mode":"driving","driving_side":"right","name":"Алберта Томе","intersections":[{"out":0,"entry":[true],"bearings":[340],"location":[19.840628,45.248112]}],"weight":8.8,"duration":5.6,"distance":13.9},{"geometry":{"coordinates":[[19.840568,45.24823],[19.841655,45.248486],[19.842472,45.248678],[19.842523,45.248691],[19.842565,45.248701],[19.842668,45.248725],[19.842721,45.248739],[19.84276,45.248748],[19.842855,45.248772],[19.842897,45.248782],[19.843792,45.249004],[19.844216,45.249122],[19.845114,45.249407],[19.845151,45.249419],[19.845268,45.249456],[19.845372,45.249488],[19.845408,45.249499],[19.846733,45.249902],[19.846914,45.249957],[19.84765,45.250181],[19.847671,45.250187],[19.847707,45.250198],[19.847781,45.25022],[19.847854,45.250242],[19.847946,45.250275],[19.84797,45.250283],[19.848831,45.250558],[19.849271,45.250701],[19.849527,45.250799],[19.849558,45.25081],[19.850753,45.251185],[19.850861,45.251219],[19.850941,45.251244],[19.851035,45.251272],[19.851076,45.251285],[19.851703,45.251476],[19.852086,45.251592],[19.852122,45.251603],[19.852325,45.25166],[19.852538,45.251719]],"type":"LineString"},"maneuver":{"bearing_after":70,"bearing_before":340,"location":[19.840568,45.24823],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"Максима Горког","intersections":[{"out":0,"in":1,"entry":[true,false,false],"bearings":[75,165,255],"location":[19.840568,45.24823]},{"out":0,"in":2,"entry":[true,true,false,false],"bearings":[75,165,255,345],"location":[19.842668,45.248725]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[75,165,255,345],"location":[19.84276,45.248748]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.845268,45.249456]},{"lanes":[{"valid":true,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":false,"indications":["right"]}],"out":0,"in":2,"entry":[true,true,false,false],"bearings":[60,150,240,330],"location":[19.847781,45.25022]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[60,150,240,330],"location":[19.847854,45.250242]},{"out":0,"in":1,"entry":[true,false,true],"bearings":[60,240,330],"location":[19.849558,45.25081]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.850941,45.251244]}],"weight":102,"duration":102,"distance":1017.5},{"geometry":{"coordinates":[[19.852538,45.251719],[19.852584,45.251624],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":160,"bearing_before":67,"location":[19.852538,45.251719],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"","intersections":[{"out":1,"in":2,"entry":[true,true,false],"bearings":[75,165,255],"location":[19.852538,45.251719]}],"weight":7,"duration":3.4,"distance":14.6},{"geometry":{"coordinates":[[19.852583,45.251593],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":0,"bearing_before":181,"location":[19.852583,45.251593],"modifier":"left","type":"arrive"},"mode":"driving","driving_side":"right","name":"","intersections":[{"in":0,"entry":[true],"bearings":[1],"location":[19.852583,45.251593]}],"weight":0,"duration":0,"distance":0}],"summary":"Алберта Томе, Максима Горког","weight":117.8,"duration":111,"distance":1046}]', null, '2023-01-23 18:57:58', 45.2438215828849, 19.841663230601487, 1, 2, 1);
-- voznja 2
INSERT INTO ride (babies_on_ride, calculated_duration, cancellation_reason, ending_latitude, ending_longitude, pets_on_ride, price, ride_status, routejson, scheduled_for, start_time, starting_latitude, starting_longitude, vehicle_id, driver_id, type_id)
VALUES (false, 20, '', 45.26379451806063, 19.83051714925389, false, 200, 'ENDED', '[{"steps":[{"geometry":{"coordinates":[[19.840628,45.248112],[19.840615,45.248137],[19.840608,45.24815],[19.840596,45.248175],[19.840568,45.24823]],"type":"LineString"},"maneuver":{"bearing_after":340,"bearing_before":0,"location":[19.840628,45.248112],"modifier":"right","type":"depart"},"mode":"driving","driving_side":"right","name":"Алберта Томе","intersections":[{"out":0,"entry":[true],"bearings":[340],"location":[19.840628,45.248112]}],"weight":8.8,"duration":5.6,"distance":13.9},{"geometry":{"coordinates":[[19.840568,45.24823],[19.841655,45.248486],[19.842472,45.248678],[19.842523,45.248691],[19.842565,45.248701],[19.842668,45.248725],[19.842721,45.248739],[19.84276,45.248748],[19.842855,45.248772],[19.842897,45.248782],[19.843792,45.249004],[19.844216,45.249122],[19.845114,45.249407],[19.845151,45.249419],[19.845268,45.249456],[19.845372,45.249488],[19.845408,45.249499],[19.846733,45.249902],[19.846914,45.249957],[19.84765,45.250181],[19.847671,45.250187],[19.847707,45.250198],[19.847781,45.25022],[19.847854,45.250242],[19.847946,45.250275],[19.84797,45.250283],[19.848831,45.250558],[19.849271,45.250701],[19.849527,45.250799],[19.849558,45.25081],[19.850753,45.251185],[19.850861,45.251219],[19.850941,45.251244],[19.851035,45.251272],[19.851076,45.251285],[19.851703,45.251476],[19.852086,45.251592],[19.852122,45.251603],[19.852325,45.25166],[19.852538,45.251719]],"type":"LineString"},"maneuver":{"bearing_after":70,"bearing_before":340,"location":[19.840568,45.24823],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"Максима Горког","intersections":[{"out":0,"in":1,"entry":[true,false,false],"bearings":[75,165,255],"location":[19.840568,45.24823]},{"out":0,"in":2,"entry":[true,true,false,false],"bearings":[75,165,255,345],"location":[19.842668,45.248725]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[75,165,255,345],"location":[19.84276,45.248748]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.845268,45.249456]},{"lanes":[{"valid":true,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":false,"indications":["right"]}],"out":0,"in":2,"entry":[true,true,false,false],"bearings":[60,150,240,330],"location":[19.847781,45.25022]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[60,150,240,330],"location":[19.847854,45.250242]},{"out":0,"in":1,"entry":[true,false,true],"bearings":[60,240,330],"location":[19.849558,45.25081]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.850941,45.251244]}],"weight":102,"duration":102,"distance":1017.5},{"geometry":{"coordinates":[[19.852538,45.251719],[19.852584,45.251624],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":160,"bearing_before":67,"location":[19.852538,45.251719],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"","intersections":[{"out":1,"in":2,"entry":[true,true,false],"bearings":[75,165,255],"location":[19.852538,45.251719]}],"weight":7,"duration":3.4,"distance":14.6},{"geometry":{"coordinates":[[19.852583,45.251593],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":0,"bearing_before":181,"location":[19.852583,45.251593],"modifier":"left","type":"arrive"},"mode":"driving","driving_side":"right","name":"","intersections":[{"in":0,"entry":[true],"bearings":[1],"location":[19.852583,45.251593]}],"weight":0,"duration":0,"distance":0}],"summary":"Алберта Томе, Максима Горког","weight":117.8,"duration":111,"distance":1046}]', null, '2023-01-23 18:57:58', 45.24087837855705, 19.84319428571367, 1, 2, 1);

-- voznja 3
INSERT INTO ride (babies_on_ride, calculated_duration, cancellation_reason, ending_latitude, ending_longitude, pets_on_ride, price, ride_status, routejson, scheduled_for, start_time, starting_latitude, starting_longitude, vehicle_id, driver_id, type_id)
VALUES (false, 30, '', 45.25482440775721, 19.83526862866636, false, 300, 'ENDED', '[{"steps":[{"geometry":{"coordinates":[[19.840628,45.248112],[19.840615,45.248137],[19.840608,45.24815],[19.840596,45.248175],[19.840568,45.24823]],"type":"LineString"},"maneuver":{"bearing_after":340,"bearing_before":0,"location":[19.840628,45.248112],"modifier":"right","type":"depart"},"mode":"driving","driving_side":"right","name":"Алберта Томе","intersections":[{"out":0,"entry":[true],"bearings":[340],"location":[19.840628,45.248112]}],"weight":8.8,"duration":5.6,"distance":13.9},{"geometry":{"coordinates":[[19.840568,45.24823],[19.841655,45.248486],[19.842472,45.248678],[19.842523,45.248691],[19.842565,45.248701],[19.842668,45.248725],[19.842721,45.248739],[19.84276,45.248748],[19.842855,45.248772],[19.842897,45.248782],[19.843792,45.249004],[19.844216,45.249122],[19.845114,45.249407],[19.845151,45.249419],[19.845268,45.249456],[19.845372,45.249488],[19.845408,45.249499],[19.846733,45.249902],[19.846914,45.249957],[19.84765,45.250181],[19.847671,45.250187],[19.847707,45.250198],[19.847781,45.25022],[19.847854,45.250242],[19.847946,45.250275],[19.84797,45.250283],[19.848831,45.250558],[19.849271,45.250701],[19.849527,45.250799],[19.849558,45.25081],[19.850753,45.251185],[19.850861,45.251219],[19.850941,45.251244],[19.851035,45.251272],[19.851076,45.251285],[19.851703,45.251476],[19.852086,45.251592],[19.852122,45.251603],[19.852325,45.25166],[19.852538,45.251719]],"type":"LineString"},"maneuver":{"bearing_after":70,"bearing_before":340,"location":[19.840568,45.24823],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"Максима Горког","intersections":[{"out":0,"in":1,"entry":[true,false,false],"bearings":[75,165,255],"location":[19.840568,45.24823]},{"out":0,"in":2,"entry":[true,true,false,false],"bearings":[75,165,255,345],"location":[19.842668,45.248725]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[75,165,255,345],"location":[19.84276,45.248748]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.845268,45.249456]},{"lanes":[{"valid":true,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":false,"indications":["right"]}],"out":0,"in":2,"entry":[true,true,false,false],"bearings":[60,150,240,330],"location":[19.847781,45.25022]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[60,150,240,330],"location":[19.847854,45.250242]},{"out":0,"in":1,"entry":[true,false,true],"bearings":[60,240,330],"location":[19.849558,45.25081]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.850941,45.251244]}],"weight":102,"duration":102,"distance":1017.5},{"geometry":{"coordinates":[[19.852538,45.251719],[19.852584,45.251624],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":160,"bearing_before":67,"location":[19.852538,45.251719],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"","intersections":[{"out":1,"in":2,"entry":[true,true,false],"bearings":[75,165,255],"location":[19.852538,45.251719]}],"weight":7,"duration":3.4,"distance":14.6},{"geometry":{"coordinates":[[19.852583,45.251593],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":0,"bearing_before":181,"location":[19.852583,45.251593],"modifier":"left","type":"arrive"},"mode":"driving","driving_side":"right","name":"","intersections":[{"in":0,"entry":[true],"bearings":[1],"location":[19.852583,45.251593]}],"weight":0,"duration":0,"distance":0}],"summary":"Алберта Томе, Максима Горког","weight":117.8,"duration":111,"distance":1046}]', null, '2023-01-23 18:57:58',45.25913135980608, 19.824620435738364, 1, 3, 1);

-- voznja 4
INSERT INTO ride (babies_on_ride, calculated_duration, cancellation_reason, ending_latitude, ending_longitude, pets_on_ride, price, ride_status, routejson, scheduled_for, start_time, starting_latitude, starting_longitude, vehicle_id, driver_id, type_id)
VALUES (false, 40, '', 45.24483439456864, 19.84566609739517, false, 400, 'ENDED', '[{"steps":[{"geometry":{"coordinates":[[19.840628,45.248112],[19.840615,45.248137],[19.840608,45.24815],[19.840596,45.248175],[19.840568,45.24823]],"type":"LineString"},"maneuver":{"bearing_after":340,"bearing_before":0,"location":[19.840628,45.248112],"modifier":"right","type":"depart"},"mode":"driving","driving_side":"right","name":"Алберта Томе","intersections":[{"out":0,"entry":[true],"bearings":[340],"location":[19.840628,45.248112]}],"weight":8.8,"duration":5.6,"distance":13.9},{"geometry":{"coordinates":[[19.840568,45.24823],[19.841655,45.248486],[19.842472,45.248678],[19.842523,45.248691],[19.842565,45.248701],[19.842668,45.248725],[19.842721,45.248739],[19.84276,45.248748],[19.842855,45.248772],[19.842897,45.248782],[19.843792,45.249004],[19.844216,45.249122],[19.845114,45.249407],[19.845151,45.249419],[19.845268,45.249456],[19.845372,45.249488],[19.845408,45.249499],[19.846733,45.249902],[19.846914,45.249957],[19.84765,45.250181],[19.847671,45.250187],[19.847707,45.250198],[19.847781,45.25022],[19.847854,45.250242],[19.847946,45.250275],[19.84797,45.250283],[19.848831,45.250558],[19.849271,45.250701],[19.849527,45.250799],[19.849558,45.25081],[19.850753,45.251185],[19.850861,45.251219],[19.850941,45.251244],[19.851035,45.251272],[19.851076,45.251285],[19.851703,45.251476],[19.852086,45.251592],[19.852122,45.251603],[19.852325,45.25166],[19.852538,45.251719]],"type":"LineString"},"maneuver":{"bearing_after":70,"bearing_before":340,"location":[19.840568,45.24823],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"Максима Горког","intersections":[{"out":0,"in":1,"entry":[true,false,false],"bearings":[75,165,255],"location":[19.840568,45.24823]},{"out":0,"in":2,"entry":[true,true,false,false],"bearings":[75,165,255,345],"location":[19.842668,45.248725]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[75,165,255,345],"location":[19.84276,45.248748]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.845268,45.249456]},{"lanes":[{"valid":true,"indications":["left"]},{"valid":true,"indications":["straight"]},{"valid":false,"indications":["right"]}],"out":0,"in":2,"entry":[true,true,false,false],"bearings":[60,150,240,330],"location":[19.847781,45.25022]},{"lanes":[{"valid":false,"indications":["left"]},{"valid":true,"indications":["straight"]}],"out":0,"in":2,"entry":[true,false,false,true],"bearings":[60,150,240,330],"location":[19.847854,45.250242]},{"out":0,"in":1,"entry":[true,false,true],"bearings":[60,240,330],"location":[19.849558,45.25081]},{"out":0,"in":2,"entry":[true,true,false,true],"bearings":[60,150,240,330],"location":[19.850941,45.251244]}],"weight":102,"duration":102,"distance":1017.5},{"geometry":{"coordinates":[[19.852538,45.251719],[19.852584,45.251624],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":160,"bearing_before":67,"location":[19.852538,45.251719],"modifier":"right","type":"turn"},"mode":"driving","driving_side":"right","name":"","intersections":[{"out":1,"in":2,"entry":[true,true,false],"bearings":[75,165,255],"location":[19.852538,45.251719]}],"weight":7,"duration":3.4,"distance":14.6},{"geometry":{"coordinates":[[19.852583,45.251593],[19.852583,45.251593]],"type":"LineString"},"maneuver":{"bearing_after":0,"bearing_before":181,"location":[19.852583,45.251593],"modifier":"left","type":"arrive"},"mode":"driving","driving_side":"right","name":"","intersections":[{"in":0,"entry":[true],"bearings":[1],"location":[19.852583,45.251593]}],"weight":0,"duration":0,"distance":0}],"summary":"Алберта Томе, Максима Горког","weight":117.8,"duration":111,"distance":1046}]', null, '2023-01-23 18:57:58', 45.2399212284593, 19.825782791363217, 1, 2, 1);

---- inserti za ride_location_names tabelu
-- voznja 1
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (1, 'Promenada', 1);
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (1, 'Koliba', 2);

-- voznja 2
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (2, 'Liman kod Mosta', 1);
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (2, 'Kod Zeleznicke stanice', 2);

-- voznja 3
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (3, 'Bulevar Kralja Petra', 1);
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (3, 'Bulevar Oslobodjenja negde sredina', 2);

-- voznja 4
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (4, 'Bulevar Cara Lazara 85', 1);
INSERT INTO ride_location_names (ride_id, location_names, address_position) VALUES (4, 'Bulevar Cara Lazara 76', 2);

---- inserti za client_ride tabelu
-- voznja 1
INSERT INTO client_ride (client_paid, driver_rated, driver_rating, vehicle_rated, vehicle_rating, client_id, ride_id)
VALUES (true, false, -1, false, -1, 4, 1);
INSERT INTO client_ride (client_paid, driver_rated, driver_rating, vehicle_rated, vehicle_rating, client_id, ride_id)
VALUES (true, false, -1, false, -1, 5, 1);

-- voznja 2
INSERT INTO client_ride (client_paid, driver_rated, driver_rating, vehicle_rated, vehicle_rating, client_id, ride_id)
VALUES (true, false, -1, false, -1, 4, 2);

-- voznja 3
INSERT INTO client_ride (client_paid, driver_rated, driver_rating, vehicle_rated, vehicle_rating, client_id, ride_id)
VALUES (true, false, -1, false, -1, 5, 3);

-- voznja 4
INSERT INTO client_ride (client_paid, driver_rated, driver_rating, vehicle_rated, vehicle_rating, client_id, ride_id)
VALUES (true, false, -1, false, -1, 4, 4);

------ END inserti za voznje ------





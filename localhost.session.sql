
select * from users;

-- CREATE TABLE users(
--     id int not null primary key auto_increment,
--     username varchar(100) not null unique,
--     password varchar(255) not null,
--     email varchar(255),
--     phone varchar(15) not null default '0000000000',
--     name varchar(255) not null,
--     role varchar(20) not null default 'ROLE_USER',
--     image varchar(255) not null,
--     gender varchar(20) not null default 'Not Selected',
--     address json not null default (JSON_OBJECT('line1', '', 'line2', '')),
--     dob varchar(20) not null default 'Not Selected'
-- );

-- CREATE TABLE doctors(
--     id int not null primary key auto_increment,
--     user_id int not null,
--     speciality varchar(50) not null,
--     degree varchar(50) not null,
--     experience varchar(10) not null,
--     about text not null,
--     available boolean default true,
--     fees float default 0.0,
--     slots_booked JSON NOT NULL,
--     foreign key (user_id) references users(id)
-- );

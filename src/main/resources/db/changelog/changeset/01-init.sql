-- Создание схемы engineers
CREATE SCHEMA IF NOT EXISTS engineers;

-- Таблица пользователей
CREATE TABLE engineers.user (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    surname TEXT,
    phone_number TEXT,
    email TEXT,
    password TEXT,
    age INTEGER,
    city TEXT,
    education TEXT,
    diploma_path text,
    status text,
    resume_path text,
    photo_path text,
    birth_date date,
    gender varchar(50)
);

-- Таблица разделов и марок
CREATE TABLE engineers.section_and_stamp (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

-- Таблица связи пользователей с разделами и марками
CREATE TABLE engineers.user_section_and_stamp (
    user_id BIGINT,
    section_and_stamp_id BIGINT,
    PRIMARY KEY (user_id, section_and_stamp_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (section_and_stamp_id) REFERENCES engineers.section_and_stamp(id)
);

-- Таблица ПО (навыки работы с ПО)
CREATE TABLE engineers.software_skill (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

-- Таблица связи пользователей с ПО
CREATE TABLE engineers.user_software_skill (
    user_id BIGINT,
    software_skill_id BIGINT,
    PRIMARY KEY (user_id, software_skill_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (software_skill_id) REFERENCES engineers.software_skill(id)
);

-- Таблица социальных сетей
CREATE TABLE engineers.social_network (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

-- Таблица связи пользователей с соцсетями
CREATE TABLE engineers.user_social_network (
    user_id BIGINT,
    social_network_id BIGINT,
    PRIMARY KEY (user_id, social_network_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (social_network_id) REFERENCES engineers.social_network(id)
);

-- Таблица проектов
CREATE TABLE engineers.project (
    id BIGSERIAL PRIMARY KEY,
    name text,
    author_id bigint,
    like_count integer,
    photo_url text,
    date_time timestamp,
    foreign key (author_id) references engineers.user(id)
);

create table engineers.collection (
    id bigserial primary key,
    name text,
    description text,
    user_id bigint,
    foreign key (user_id) references engineers.user(id)
);
-- Таблица избранного
CREATE TABLE engineers.favourites (
    user_id BIGINT,
    project_id BIGINT,
    collection_id BIGINT,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (project_id) REFERENCES engineers.project(id),
    foreign key (collection_id) references engineers.collection(id)
);

-- Таблица курсов
CREATE TABLE engineers.course (
    id BIGSERIAL PRIMARY KEY,
    name TEXT unique,
    price text,
    duration text,
    start_date text,
    hours text,
    format text,
    who_whom text,
    what_master text,
    price_full text
);

-- Таблица тегов
CREATE TABLE engineers.tag (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

-- Таблица связи курсов с тегами
CREATE TABLE engineers.course_tag (
    course_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (course_id, tag_id),
    FOREIGN KEY (course_id) REFERENCES engineers.course(id),
    FOREIGN KEY (tag_id) REFERENCES engineers.tag(id)
);

-- Таблица модулей
CREATE TABLE engineers.module (
    id BIGSERIAL PRIMARY KEY,
    number INTEGER,
    name TEXT
);

-- Таблица уроков
CREATE TABLE engineers.lesson (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    module_id BIGINT,
    author_id bigint,
    number integer,
    FOREIGN KEY (module_id) REFERENCES engineers.module(id),
    foreign key (author_id) references engineers.user(id)
);

-- Таблица контента
CREATE TABLE engineers.content (
    id BIGSERIAL PRIMARY KEY,
    content_type TEXT,
    content TEXT,
    content_url TEXT,
    "order" INTEGER,
    lesson_id BIGINT,
    FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id)
);

-- Таблица связи пользователей с курсами
CREATE TABLE engineers.user_course (
    user_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (course_id) REFERENCES engineers.course(id)
);

-- Таблица домашних заданий
CREATE TABLE engineers.homework (
    lesson_id BIGINT,
    user_id BIGINT,
    attachment BYTEA,
    result BOOLEAN,
    teacher_id BIGINT,
    PRIMARY KEY (lesson_id, user_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (teacher_id) REFERENCES engineers.user(id),
    FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id)
);

-- Таблица связи уроков с пользователями
CREATE TABLE engineers.lesson_user (
    lesson_id BIGINT,
    user_id BIGINT,
    is_here boolean,
    PRIMARY KEY (lesson_id, user_id),
    FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id)
);

-- Таблица уведомлений
CREATE TABLE engineers.notification (
    id BIGSERIAL PRIMARY KEY,
    notification_text TEXT,
    notification_user_id bigint,
    project_id bigint,
    date_time timestamp,
    is_watched boolean,
    unique(notification_user_id, project_id)
);

-- Таблица связи пользователей с уведомлениями
CREATE TABLE engineers.notification_user (
    user_id BIGINT,
    notification_id BIGINT,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES engineers.user(id),
    FOREIGN KEY (notification_id) REFERENCES engineers.notification(id)
);

create table engineers.refresh_token (
    id bigint primary key generated by default as identity ,
    expire_date timestamp(6) with time zone,
    token varchar (255),
    user_id bigint UNIQUE,
    foreign key (user_id) references engineers.user(id)
);

create table engineers.password_reset_token (
    id bigint primary key generated by default as identity ,
    expire_date timestamp(6) with time zone,
    token varchar (255),
    user_id bigint UNIQUE,
    foreign key (user_id) references engineers.user(id)
);

create table engineers.course_module (
    course_id bigint,
    module_id bigint,
    primary key (course_id, module_id),
    foreign key (course_id) references engineers.course(id),
    foreign key (module_id) references engineers.module(id)
);

create table project_section_and_stamp (
    project_id bigint,
    section_and_stamp_id bigint,
    primary key(project_id, section_and_stamp_id),
    foreign key (project_id) references engineers.project(id),
    foreign key (section_and_stamp_id) references engineers.section_and_stamp(id)
);

create table project_software_skill (
    project_id bigint,
    software_skill_id bigint,
    primary key (project_id, software_skill_id),
    foreign key (project_id) references engineers.project(id),
    foreign key (software_skill_id) references engineers.software_skill(id)
);

create table project_user_like (
    project_id bigint,
    user_id bigint,
    primary key (project_id, user_id),
    foreign key (project_id) references engineers.project(id),
    foreign key (user_id) references engineers.user(id)
);


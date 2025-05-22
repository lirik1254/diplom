--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: engineers; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA engineers;


ALTER SCHEMA engineers OWNER TO postgres;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS '';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: collection; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.collection (
    id bigint NOT NULL,
    name text,
    description text,
    user_id bigint
);


ALTER TABLE engineers.collection OWNER TO postgres;

--
-- Name: collection_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.collection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.collection_id_seq OWNER TO postgres;

--
-- Name: collection_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.collection_id_seq OWNED BY engineers.collection.id;


--
-- Name: companies; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.companies (
    id bigint NOT NULL,
    name text,
    status text,
    city text,
    photo_path text
);


ALTER TABLE engineers.companies OWNER TO postgres;

--
-- Name: companies_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.companies_id_seq OWNER TO postgres;

--
-- Name: companies_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.companies_id_seq OWNED BY engineers.companies.id;


--
-- Name: content; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.content (
    id bigint NOT NULL,
    content_type text,
    content text,
    content_url text,
    "order" integer,
    lesson_id bigint
);


ALTER TABLE engineers.content OWNER TO postgres;

--
-- Name: content_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.content_id_seq OWNER TO postgres;

--
-- Name: content_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.content_id_seq OWNED BY engineers.content.id;


--
-- Name: course; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.course (
    id bigint NOT NULL,
    name text,
    price text,
    duration text,
    start_date text,
    hours text,
    format text,
    who_whom text,
    what_master text,
    price_full text,
    show_date_bought_course text
);


ALTER TABLE engineers.course OWNER TO postgres;

--
-- Name: course_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.course_id_seq OWNER TO postgres;

--
-- Name: course_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.course_id_seq OWNED BY engineers.course.id;


--
-- Name: course_tag; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.course_tag (
    course_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE engineers.course_tag OWNER TO postgres;

--
-- Name: favourites; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.favourites (
    user_id bigint NOT NULL,
    project_id bigint NOT NULL,
    collection_id bigint
);


ALTER TABLE engineers.favourites OWNER TO postgres;

--
-- Name: homework; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.homework (
    lesson_id bigint NOT NULL,
    user_id bigint NOT NULL,
    attachment bytea,
    result boolean,
    teacher_id bigint
);


ALTER TABLE engineers.homework OWNER TO postgres;

--
-- Name: lesson; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.lesson (
    id bigint NOT NULL,
    name text,
    module_id bigint,
    author_id bigint,
    number integer
);


ALTER TABLE engineers.lesson OWNER TO postgres;

--
-- Name: lesson_content; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.lesson_content (
    id bigint NOT NULL,
    lesson_text text,
    video_url text,
    lesson_task_text text,
    lesson_id bigint
);


ALTER TABLE engineers.lesson_content OWNER TO postgres;

--
-- Name: lesson_content_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.lesson_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.lesson_content_id_seq OWNER TO postgres;

--
-- Name: lesson_content_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.lesson_content_id_seq OWNED BY engineers.lesson_content.id;


--
-- Name: lesson_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.lesson_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.lesson_id_seq OWNER TO postgres;

--
-- Name: lesson_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.lesson_id_seq OWNED BY engineers.lesson.id;


--
-- Name: module; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.module (
    id bigint NOT NULL,
    number integer,
    name text,
    course_id bigint
);


ALTER TABLE engineers.module OWNER TO postgres;

--
-- Name: module_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.module_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.module_id_seq OWNER TO postgres;

--
-- Name: module_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.module_id_seq OWNED BY engineers.module.id;


--
-- Name: notification; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.notification (
    notification_text text,
    notification_user_id bigint NOT NULL,
    project_id bigint NOT NULL,
    id bigint NOT NULL,
    date_time timestamp without time zone,
    is_watched boolean
);


ALTER TABLE engineers.notification OWNER TO postgres;

--
-- Name: notification_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

ALTER TABLE engineers.notification ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME engineers.notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: notification_user; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.notification_user (
    user_id bigint NOT NULL,
    notification_id bigint NOT NULL
);


ALTER TABLE engineers.notification_user OWNER TO postgres;

--
-- Name: password_reset_token; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.password_reset_token (
    id bigint NOT NULL,
    expire_date timestamp(6) with time zone,
    token character varying(255),
    user_id bigint
);


ALTER TABLE engineers.password_reset_token OWNER TO postgres;

--
-- Name: password_reset_token_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

ALTER TABLE engineers.password_reset_token ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME engineers.password_reset_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: project; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.project (
    id bigint NOT NULL,
    name text,
    author_id bigint,
    like_count integer,
    photo_url text,
    date_time timestamp without time zone
);


ALTER TABLE engineers.project OWNER TO postgres;

--
-- Name: project_content; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.project_content (
    id bigint NOT NULL,
    content_type text,
    content text,
    content_url text,
    content_order integer,
    project_id bigint
);


ALTER TABLE engineers.project_content OWNER TO postgres;

--
-- Name: project_content_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.project_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.project_content_id_seq OWNER TO postgres;

--
-- Name: project_content_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.project_content_id_seq OWNED BY engineers.project_content.id;


--
-- Name: project_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.project_id_seq OWNER TO postgres;

--
-- Name: project_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.project_id_seq OWNED BY engineers.project.id;


--
-- Name: project_section_and_stamp; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.project_section_and_stamp (
    project_id bigint NOT NULL,
    section_and_stamp_id bigint NOT NULL
);


ALTER TABLE engineers.project_section_and_stamp OWNER TO postgres;

--
-- Name: project_software_skill; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.project_software_skill (
    project_id bigint NOT NULL,
    software_skill_id bigint NOT NULL
);


ALTER TABLE engineers.project_software_skill OWNER TO postgres;

--
-- Name: project_user_like; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.project_user_like (
    project_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE engineers.project_user_like OWNER TO postgres;

--
-- Name: refresh_token; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.refresh_token (
    id bigint NOT NULL,
    expire_date timestamp(6) with time zone,
    token character varying(255),
    user_id bigint
);


ALTER TABLE engineers.refresh_token OWNER TO postgres;

--
-- Name: refresh_token_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

ALTER TABLE engineers.refresh_token ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME engineers.refresh_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: section_and_stamp; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.section_and_stamp (
    id bigint NOT NULL,
    name text
);


ALTER TABLE engineers.section_and_stamp OWNER TO postgres;

--
-- Name: section_and_stamp_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.section_and_stamp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.section_and_stamp_id_seq OWNER TO postgres;

--
-- Name: section_and_stamp_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.section_and_stamp_id_seq OWNED BY engineers.section_and_stamp.id;


--
-- Name: software_skill; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.software_skill (
    id bigint NOT NULL,
    name text
);


ALTER TABLE engineers.software_skill OWNER TO postgres;

--
-- Name: software_skill_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.software_skill_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.software_skill_id_seq OWNER TO postgres;

--
-- Name: software_skill_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.software_skill_id_seq OWNED BY engineers.software_skill.id;


--
-- Name: tag; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.tag (
    id bigint NOT NULL,
    name text
);


ALTER TABLE engineers.tag OWNER TO postgres;

--
-- Name: tag_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.tag_id_seq OWNER TO postgres;

--
-- Name: tag_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.tag_id_seq OWNED BY engineers.tag.id;


--
-- Name: user; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers."user" (
    id bigint NOT NULL,
    name text,
    surname text,
    phone_number text,
    email text,
    password text,
    city text,
    diploma_path text,
    status text,
    resume_path text,
    photo_path text,
    birth_date date,
    gender character varying(50),
    telegram text,
    about text,
    hide_birthday boolean,
    is_public boolean
);


ALTER TABLE engineers."user" OWNER TO postgres;

--
-- Name: user_course; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.user_course (
    user_id bigint NOT NULL,
    course_id bigint NOT NULL,
    last_lesson_id bigint
);


ALTER TABLE engineers.user_course OWNER TO postgres;

--
-- Name: user_education; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.user_education (
    id bigint NOT NULL,
    user_id bigint,
    education text
);


ALTER TABLE engineers.user_education OWNER TO postgres;

--
-- Name: user_education_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.user_education_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.user_education_id_seq OWNER TO postgres;

--
-- Name: user_education_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.user_education_id_seq OWNED BY engineers.user_education.id;


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.user_id_seq OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.user_id_seq OWNED BY engineers."user".id;


--
-- Name: user_section_and_stamp; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.user_section_and_stamp (
    user_id bigint NOT NULL,
    section_and_stamp_id bigint NOT NULL
);


ALTER TABLE engineers.user_section_and_stamp OWNER TO postgres;

--
-- Name: user_social_network; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.user_social_network (
    id bigint NOT NULL,
    user_id bigint,
    social_network text
);


ALTER TABLE engineers.user_social_network OWNER TO postgres;

--
-- Name: user_social_network_id_seq; Type: SEQUENCE; Schema: engineers; Owner: postgres
--

CREATE SEQUENCE engineers.user_social_network_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE engineers.user_social_network_id_seq OWNER TO postgres;

--
-- Name: user_social_network_id_seq; Type: SEQUENCE OWNED BY; Schema: engineers; Owner: postgres
--

ALTER SEQUENCE engineers.user_social_network_id_seq OWNED BY engineers.user_social_network.id;


--
-- Name: user_software_skill; Type: TABLE; Schema: engineers; Owner: postgres
--

CREATE TABLE engineers.user_software_skill (
    user_id bigint NOT NULL,
    software_skill_id bigint NOT NULL
);


ALTER TABLE engineers.user_software_skill OWNER TO postgres;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- Name: collection id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.collection ALTER COLUMN id SET DEFAULT nextval('engineers.collection_id_seq'::regclass);


--
-- Name: companies id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.companies ALTER COLUMN id SET DEFAULT nextval('engineers.companies_id_seq'::regclass);


--
-- Name: content id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.content ALTER COLUMN id SET DEFAULT nextval('engineers.content_id_seq'::regclass);


--
-- Name: course id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course ALTER COLUMN id SET DEFAULT nextval('engineers.course_id_seq'::regclass);


--
-- Name: lesson id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson ALTER COLUMN id SET DEFAULT nextval('engineers.lesson_id_seq'::regclass);


--
-- Name: lesson_content id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson_content ALTER COLUMN id SET DEFAULT nextval('engineers.lesson_content_id_seq'::regclass);


--
-- Name: module id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.module ALTER COLUMN id SET DEFAULT nextval('engineers.module_id_seq'::regclass);


--
-- Name: project id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project ALTER COLUMN id SET DEFAULT nextval('engineers.project_id_seq'::regclass);


--
-- Name: project_content id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_content ALTER COLUMN id SET DEFAULT nextval('engineers.project_content_id_seq'::regclass);


--
-- Name: section_and_stamp id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.section_and_stamp ALTER COLUMN id SET DEFAULT nextval('engineers.section_and_stamp_id_seq'::regclass);


--
-- Name: software_skill id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.software_skill ALTER COLUMN id SET DEFAULT nextval('engineers.software_skill_id_seq'::regclass);


--
-- Name: tag id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.tag ALTER COLUMN id SET DEFAULT nextval('engineers.tag_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers."user" ALTER COLUMN id SET DEFAULT nextval('engineers.user_id_seq'::regclass);


--
-- Name: user_education id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_education ALTER COLUMN id SET DEFAULT nextval('engineers.user_education_id_seq'::regclass);


--
-- Name: user_social_network id; Type: DEFAULT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_social_network ALTER COLUMN id SET DEFAULT nextval('engineers.user_social_network_id_seq'::regclass);


--
-- Data for Name: collection; Type: TABLE DATA; Schema: engineers; Owner: postgres
--



--
-- Data for Name: companies; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.companies VALUES (1, 'Велесстрой', 'Активно растём', 'Москва', 'veles.jpg');
INSERT INTO engineers.companies VALUES (2, 'Стройтранснефтегаз', 'Удалённая работа', 'Санкт-Петербург', 'STNG.jpg');
INSERT INTO engineers.companies VALUES (4, 'Кайрос Инжинирнг', 'Ищем специалистов', 'Пермь', 'kairos.jpg');


--
-- Data for Name: content; Type: TABLE DATA; Schema: engineers; Owner: postgres
--



--
-- Data for Name: course; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.course VALUES (9, 'Технологии информационного моделирования.
Renga: архитектура, конструктив и инженерные сети', 'От 10.000 ₽', '2 месяца', 'Старт каждый понедельник', '72 часа', 'Асинхронное обучение - онлайн формат

Синхронное обучение - гибридный формат', 'Студенты строительных специальностей (ВУЗы и колледжи)

Проектировщики (архитектурные, конструктивные, инженерные разделы)

Главные инженеры проектов и менеджеры проектов

Научно-педагогические работники (ВУЗы и колледжи)', 'Понимание основ и принципов информационного моделирования в ПО Renga.

Умение работать со свойствами элементов ЦИМ, спецификациями и таблицами.

Навыки оформления проектной документации в Renga.', '10.000 ₽ - онлайн
18.000 ₽ - гибрид', NULL);
INSERT INTO engineers.course VALUES (7, 'Применение ТИМ в сметном деле (BIM WIZARD)', 'От 10.000 ₽', '2 месяца', 'Старт каждый понедельник с 21 апреля', '72 часа', 'Асинхронное обучение - онлайн формат

Синхронное обучение - гибридный формат', 'Студенты строительных специальностей (ВУЗы и колледжи)

Сметчики

Руководители проектировочных команд

Технические заказчики', 'Понимание изменений в законодательстве, касающихся экспертизы и ТИМ

Отсутствие ошибок при проектировании

Улучшение качества проектной документации', '10.000 ₽ - онлайн
18.000 ₽ - гибрид', NULL);
INSERT INTO engineers.course VALUES (4, 'Специалист в сфере информационного моделирования', '50.000 ₽', '6 месяцев', 'Старт по мере комплектования группы', '250 часов', 'Синхронное обучение - гибридный формат', 'Специалисты строительной отрасли

Выпускники строительных ВУЗов и СПО

Главные инженеры проектов и руководители

Преподаватели и научные работники', 'Глубокое понимание основ и принципов информационного моделирования
Создание

и редактирование моделей, управление свойствами элементов,формирование
спецификаций и таблиц

Интеграция ТИМ в собственные профессиональные задачи', '50.000 ₽', NULL);
INSERT INTO engineers.course VALUES (6, 'Практическое освоение STDL', 'От 700 ₽', '1 неделя', 'Старт каждый понедельник', '8 часов', 'Асинхронное обучение - онлайн формат', 'Студенты строительных специальностей (ВУЗы и колледжи)

Проектировщики (архитектурные, конструктивные, инженерные разделы)

Специалисты смежных профессий в строительстве

Научно-педагогические работники (ВУЗы и колледжи)', 'Начальные навыки разработки каталогов элементов с использованием STDL

Навыки чтения и интерпретации геометрических данных из файлов, используя инструменты STDL

Навыки создания и сохранения файлов с результатами обработки данных, включая диагностику и устранение ошибок', '700 ₽ без сертификата
1.200 ₽ с сертификатом', NULL);
INSERT INTO engineers.course VALUES (8, 'Законодательство в области экспертизы и ТИМ', 'От 3.000 ₽', '3 недели', 'Старт каждый понедельник', '24 часа', 'Асинхронное обучение - онлайн формат

Синхронное обучение - гибридный формат', 'Студенты строительных специальностей (ВУЗы и колледжи)

Эксперты в области строительства и проектирования, осуществляющие экспертизу проектной документации

Проектировщики и архитекторы, работающие с ТИМ

Представители заказчика (девелоперы, застройщики)', 'Понимание изменений в законодательстве, касающихся экспертизы и ТИМ

Отсутствие ошибок при проектировании

Улучшение качества проектной документации', '3.000 ₽ - онлайн
7.000 ₽ - гибрид', NULL);
INSERT INTO engineers.course VALUES (5, 'Практическая подготовка студентов СПО в рамках направления "Информационное моделирование"', '15.000 ₽', '—', 'Старт по мере комплектования группы', '72 часа', 'Синхронное обучение - гибридный формат', 'Преподаватели строительных специальностей СПО

Методисты и руководители образовательных программ', 'Понимание основ и принципов информационного моделирования

Умение работать со свойствами элементов ЦИМ, спецификациями и таблицами

Интеграция ТИМ в учебный процесс', '15.000 ₽', NULL);
INSERT INTO engineers.course VALUES (10, 'Курс по Android', 'От 5.000 ₽', '6 недель', 'Старт каждый вторник ', '48 часов', 'Асинхронное обучение - онлайн формат', 'Студенты технических специальностей, увлекающиеся программированием', 'Понимание основ Java-разработки

Освоение принципов работы с файловым хранилищем и удалённым хранилищем FireBase

Погружение в Android SDK', '5.000 ₽ - Онлайн формат', '12.03 - 25.04');
INSERT INTO engineers.course VALUES (11, 'Figma для начинающих', 'От 7.000 Р', '2 месяца', 'Старт по мере комплектования группы', '72 часа', 'Синхронное обучение - гибридный формат', 'Студенты технических специальностей

Преподаватели технических специальностей', 'Понимание основ работы в Figma

Создание своего pet-проекта', '7.000 ₽ - Онлайн формат, 10.000 ₽ - Гибрид', '01.04 - 10.05');


--
-- Data for Name: course_tag; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.course_tag VALUES (4, 1);
INSERT INTO engineers.course_tag VALUES (5, 2);
INSERT INTO engineers.course_tag VALUES (6, 3);
INSERT INTO engineers.course_tag VALUES (7, 2);
INSERT INTO engineers.course_tag VALUES (8, 2);
INSERT INTO engineers.course_tag VALUES (9, 2);


--
-- Data for Name: favourites; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.favourites VALUES (34, 1, NULL);


--
-- Data for Name: homework; Type: TABLE DATA; Schema: engineers; Owner: postgres
--



--
-- Data for Name: lesson; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.lesson VALUES (5, 'Урок 2.1: Навигация между экранами и передача данных', 4, 18, 1);
INSERT INTO engineers.lesson VALUES (6, 'Урок 2.2: Хранение данных с SharedPreferences', 4, 13, 2);
INSERT INTO engineers.lesson VALUES (7, 'Урок 1.1: Структура Android-проекта и жизненный цикл Activity', 3, 16, 1);
INSERT INTO engineers.lesson VALUES (8, 'Урок 1.2: Вёрстка интерфейса с XML и работа с View', 3, 17, 2);
INSERT INTO engineers.lesson VALUES (10, 'Урок 1.2: Цвета, шрифты и стили', 5, 18, 2);
INSERT INTO engineers.lesson VALUES (9, 'Урок 1.1: Интерфейс Figma и создание первого макета', 5, 13, 1);
INSERT INTO engineers.lesson VALUES (11, 'Урок 2.1: Компоненты и Auto Layout', 6, 17, 1);
INSERT INTO engineers.lesson VALUES (12, 'Урок 2.2: Прототипирование и интерактивные переходы', 6, 13, 2);


--
-- Data for Name: lesson_content; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.lesson_content VALUES (1, 'В этом видео мы научимся создавать переходы между экранами в Android-приложении, используя Intent. Также разберёмся, как передавать данные из одного Activity в другое, и рассмотрим методы putExtra и getIntent().getStringExtra().', 'android_1.mp4', 'Создайте два экрана: один с полем ввода и кнопкой, второй — с отображением введённого текста. При нажатии на кнопку должен происходить переход на второй экран с передачей введённой информации.', 5);
INSERT INTO engineers.lesson_content VALUES (7, 'Изучим, как использовать компоненты в Figma — переиспользуемые элементы UI, такие как кнопки или карточки. Познакомимся с Auto Layout — автоматическим размещением и выравниванием элементов.', 'figma_1.mp4', 'Создайте компонент "карточка товара" с Auto Layout. Переиспользуйте его на экране списка товаров, изменяя только текст и изображения.', 11);
INSERT INTO engineers.lesson_content VALUES (5, 'Изучим, как подключать кастомные шрифты, использовать палитру цветов и оформлять элементы интерфейса через стили и темы в styles.xml. Обсудим принципы единого визуального стиля.', 'figma_1.mp4', 'Примените собственную цветовую схему, шрифт и стиль ко всему интерфейсу входа, созданному в прошлом задании. Используйте colors.xml и styles.xml для централизованного управления.', 10);
INSERT INTO engineers.lesson_content VALUES (8, 'Научимся связывать экраны в Figma для создания прототипов. Добавим интерактивность — переходы, навигацию по кнопкам, анимации переходов между фреймами.', 'figma_2.mp4', 'Свяжите макеты экранов входа и профиля с помощью интерактивных переходов. Настройте навигацию так, чтобы при клике по кнопке "Войти" открывался профиль.', 12);
INSERT INTO engineers.lesson_content VALUES (2, 'Мы изучим, как сохранять данные на устройстве при помощи SharedPreferences. Разберёмся, как сохранять значения, восстанавливать их при перезапуске приложения и удалять при необходимости.', 'android_2.mp4', 'Реализуйте экран с переключателем темы (светлая/тёмная). Состояние переключателя должно сохраняться с помощью SharedPreferences и применяться при следующем запуске приложения.', 6);
INSERT INTO engineers.lesson_content VALUES (6, 'Познакомимся с интерфейсом Figma: панели инструментов, фреймы, фигуры, текст. Создадим первый макет экрана мобильного приложения и научимся экспортировать элементы.', 'figma_2.mp4', 'Сделайте в Figma макет экрана входа', 9);
INSERT INTO engineers.lesson_content VALUES (3, 'Познакомимся с базовой структурой Android-проекта: Manifest, res, java, Gradle. Также изучим жизненный цикл Activity и методы onCreate, onStart, onResume, onPause, onStop, onDestroy.', 'android_1.mp4', 'Создайте новое приложение, выведите сообщения (Toast или Log) при вызове каждого метода жизненного цикла. Запустите и закройте приложение, наблюдая за последовательностью вызовов.', 7);
INSERT INTO engineers.lesson_content VALUES (4, 'Научимся создавать пользовательский интерфейс с помощью XML: TextView, EditText, Button. Разберёмся с размещением элементов с использованием LinearLayout, ConstraintLayout.', 'android_2.mp4', 'Сверстайте экран входа в приложение: поля "Логин" и "Пароль" и кнопка "Войти". При нажатии на кнопку отображается Toast с введёнными данными.', 8);


--
-- Data for Name: module; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.module VALUES (4, 2, 'Модуль 2: Работа с данными и навигацией', 10);
INSERT INTO engineers.module VALUES (3, 1, 'Модуль 1: Основы Android-разработки', 10);
INSERT INTO engineers.module VALUES (5, 1, 'Модуль 1: Введение в Figma и основы дизайна', 11);
INSERT INTO engineers.module VALUES (6, 2, 'Модуль 2: Компоненты, авто-лейаут и прототипирование', 11);


--
-- Data for Name: notification; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.notification OVERRIDING SYSTEM VALUE VALUES ('курса автор оценила ваш проект "Проект малоэтажного дома "Технологии информационного моделирования""', 14, 1, 9, '2025-04-17 23:00:44.664365', false);
INSERT INTO engineers.notification OVERRIDING SYSTEM VALUE VALUES ('Шульжик Кирилл оценила ваш проект "Проект жилого дома смежной этажности галерейного типа"', 34, 2, 103, '2025-05-13 18:42:40.900584', false);
INSERT INTO engineers.notification OVERRIDING SYSTEM VALUE VALUES ('Шульжик Кирилл оценила ваш проект "Проект малоэтажного дома "Технологии информационного моделирования""', 34, 1, 104, '2025-05-13 18:42:42.038683', false);
INSERT INTO engineers.notification OVERRIDING SYSTEM VALUE VALUES ('mySurname MyName оценила ваш проект "Проект жилого дома смежной этажности галерейного типа"', 13, 2, 93, '2025-05-10 21:30:33.245169', false);


--
-- Data for Name: notification_user; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.notification_user VALUES (13, 9);
INSERT INTO engineers.notification_user VALUES (14, 93);
INSERT INTO engineers.notification_user VALUES (14, 103);
INSERT INTO engineers.notification_user VALUES (13, 104);


--
-- Data for Name: password_reset_token; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.password_reset_token VALUES (34, '2025-05-07 02:23:36.757456+05', '75eb1e6d-90b4-46a7-bace-17d374c67acd', 15);


--
-- Data for Name: project; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.project VALUES (1, 'Проект малоэтажного дома "Технологии информационного моделирования"', 34, 3, 'технологии_информационного.jpg', '2023-05-15 14:30:00');
INSERT INTO engineers.project VALUES (2, 'Проект жилого дома смежной этажности галерейного типа', 14, 1, 'галерейный_тип.jpg', '2022-12-31 23:59:59');


--
-- Data for Name: project_content; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.project_content VALUES (1, 'text', 'Цель: разработка альбома чертежей двухэтажного индивидуального жилого дома
Дисциплина: МДК 03.01.т. Основы инженерной графики; МДК 01.05.т. Инженерные сети и оборудование зданий (ИСиО)
Сроки: сентябрь 2023-апрель 2024; сентябрь-декабрь 2024 (дополнение, разделы ВК и ОВ)


Характеристики дома:
-Стены: наружные - 640 мм, внутренние несущие - 380 мм, перегородки - 120 мм. Материал - кирпич полнотелый и облицовочный
-Перекрытия: цокольное и междуэтажное - железобетонная плита многопустотная (тип ПК) толщиной 220 мм; чердачное - по деревянным лагам
-Фундамент: ленточный сборный из блоков ФБС по фундаментным плитам ФЛ
-Кровля: скатная. Стропила - деревянные, материал кровли - профилированный стальной лист. Чердак не эксплуатируемый
-Окна: металлопластиковые (ПВХ);
-Двери: металлопластиковые (входные), деревянные (межкомнатные)
Высота этажа - 3,3 м
Состав первого этажа: прихожая, холл, гардероб, санузел, столовая, кухня;
Состав второго этажа: санузел, холл, три спальни.', NULL, 1, 1);
INSERT INTO engineers.project_content VALUES (2, 'photo', NULL, 'first_photo.jpg', 2, 1);
INSERT INTO engineers.project_content VALUES (3, 'photo', NULL, 'second_photo.jpg', 3, 1);
INSERT INTO engineers.project_content VALUES (4, 'text', 'План и разрез лестницы', NULL, 4, 1);
INSERT INTO engineers.project_content VALUES (5, 'photo', NULL, 'plan_photo.jpg', 5, 1);
INSERT INTO engineers.project_content VALUES (6, 'text', 'Дата проектирования: 2024

Полную информацию вы можете посмотреть перейдя по ссылке: https://disk.yandex.ru/d/7HMPz_JylHI7RQ', '', 1, 2);
INSERT INTO engineers.project_content VALUES (7, 'photo', NULL, 'third_1.jpg', 2, 2);
INSERT INTO engineers.project_content VALUES (8, 'photo', NULL, 'third_2.jpg', 3, 2);
INSERT INTO engineers.project_content VALUES (9, 'photo', NULL, 'third_3.jpg', 4, 2);


--
-- Data for Name: project_section_and_stamp; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.project_section_and_stamp VALUES (1, 1);
INSERT INTO engineers.project_section_and_stamp VALUES (2, 1);


--
-- Data for Name: project_software_skill; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.project_software_skill VALUES (1, 7);
INSERT INTO engineers.project_software_skill VALUES (2, 1);
INSERT INTO engineers.project_software_skill VALUES (2, 3);


--
-- Data for Name: project_user_like; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.project_user_like VALUES (1, 14);
INSERT INTO engineers.project_user_like VALUES (1, 13);
INSERT INTO engineers.project_user_like VALUES (1, 34);
INSERT INTO engineers.project_user_like VALUES (2, 34);


--
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.refresh_token VALUES (315, '2025-05-28 23:17:55.535747+05', '38f33009-f423-443f-8ac9-1aff1135257e', 34);
INSERT INTO engineers.refresh_token VALUES (230, '2025-05-19 22:54:22.653083+05', '3ad6fa39-4117-4522-b34a-01f46c4c574b', 13);
INSERT INTO engineers.refresh_token VALUES (234, '2025-05-19 23:34:16.698241+05', '1f843717-0377-4445-86aa-c55200433f6d', 14);


--
-- Data for Name: section_and_stamp; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.section_and_stamp VALUES (1, 'ГП');
INSERT INTO engineers.section_and_stamp VALUES (2, 'АС');
INSERT INTO engineers.section_and_stamp VALUES (3, 'АР');
INSERT INTO engineers.section_and_stamp VALUES (4, 'ВК');
INSERT INTO engineers.section_and_stamp VALUES (5, 'ВС');
INSERT INTO engineers.section_and_stamp VALUES (6, 'ГР');
INSERT INTO engineers.section_and_stamp VALUES (7, 'КЖ');
INSERT INTO engineers.section_and_stamp VALUES (8, 'КМ');
INSERT INTO engineers.section_and_stamp VALUES (9, 'КД');
INSERT INTO engineers.section_and_stamp VALUES (10, 'НВ');
INSERT INTO engineers.section_and_stamp VALUES (11, 'НК');
INSERT INTO engineers.section_and_stamp VALUES (12, 'НВК');
INSERT INTO engineers.section_and_stamp VALUES (13, 'ОВ');
INSERT INTO engineers.section_and_stamp VALUES (14, 'ТХ');
INSERT INTO engineers.section_and_stamp VALUES (15, 'ЭС');
INSERT INTO engineers.section_and_stamp VALUES (16, 'ЭН');
INSERT INTO engineers.section_and_stamp VALUES (17, 'ЭМ');
INSERT INTO engineers.section_and_stamp VALUES (18, 'ЭО');


--
-- Data for Name: software_skill; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.software_skill VALUES (1, 'Allplan');
INSERT INTO engineers.software_skill VALUES (2, 'Renga');
INSERT INTO engineers.software_skill VALUES (3, 'Revit');
INSERT INTO engineers.software_skill VALUES (4, 'Компас 3D');
INSERT INTO engineers.software_skill VALUES (5, 'BIM WIZARD');
INSERT INTO engineers.software_skill VALUES (6, 'ArchiCAD');
INSERT INTO engineers.software_skill VALUES (7, 'AutoCAD');
INSERT INTO engineers.software_skill VALUES (8, 'nanoCAD');
INSERT INTO engineers.software_skill VALUES (9, 'Pilot Ice');


--
-- Data for Name: tag; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.tag VALUES (1, 'Профессиональная переподготовка');
INSERT INTO engineers.tag VALUES (2, 'Базовый курс повышения квалификации');
INSERT INTO engineers.tag VALUES (3, 'Семинар-практикум');


--
-- Data for Name: user; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers."user" VALUES (13, 'MyName', 'mySurname', NULL, 'kirill_shulzhik@mail.ru', '$2a$10$o2eNoHvyDc8hUBbKgNYC1.3c4uEMeWsE9XKThptdks/bbf44VC2d6', 'Екатеринбург', 'C:\Users\zhash\IdeaProjects\diplom\uploads\diploma_aaf3d15c-2e90-430e-b592-e8e8de5de8bc_PP presentation.pptx', 'Открыт к предложениям', 'resume_17440628026216ab979c2-e6e6-478e-877c-980f1440b966___PP presentation.pptx', 'photo_3.jpg', '2005-05-17', NULL, NULL, NULL, NULL, true);
INSERT INTO engineers."user" VALUES (34, 'Кирилл', 'Шульжик', '89223425236', 'power_tanker@mail.ru', '$2a$10$5.uuyuv./jhYNTekK4RIyedpyLBKzwaNRPQ3ZWuChBWadI2y9heBa', 'Пермь', 'Диплом.zip', 'Ищу работу', 'Еще один важный файл.pdf', 'content://media/external/images/media/83f12d4c13-6178-4a3e-b8f0-6251ed451da8', '2005-05-17', 'Мужской', 'https://t.me/shulzhkv', 'Студент пермского кампуса ВШЭ, студент Академии Бекенда, студент курса Java-middle на базе НИУ-ВШЭ, не знаю что я делаю в инженерном приложении', false, true);
INSERT INTO engineers."user" VALUES (18, 'Дмитрий', 'Сальников', NULL, NULL, NULL, 'Пермь', NULL, 'Ищу стажировку', NULL, 'photo_6.jpg', '2005-05-17', NULL, NULL, NULL, NULL, NULL);
INSERT INTO engineers."user" VALUES (15, 'Кирилл', 'Шульжик', '89223428991', 'kvshulzhik@edu.hse.ru', '$2a$10$geYa9qhSjQr3MC40lQE.luaKy5tTrsjjWRtjA2t7Iw6OPiED7MmtW', 'Пермь', NULL, 'Ищу работу', NULL, 'автор.png', '2005-05-17', NULL, NULL, NULL, NULL, NULL);
INSERT INTO engineers."user" VALUES (16, 'Виктория ', 'Иванченкова', NULL, NULL, NULL, 'Екатеринбург', NULL, 'Открыт к предложениям', NULL, 'photo_4.jpg', '2005-05-17', NULL, NULL, '', NULL, NULL);
INSERT INTO engineers."user" VALUES (14, 'автор', 'курса', NULL, 'zalupka@mail.ru', '$2a$10$DnUzgZzyHVq0sNklDjId/OjebsUcp5RLzf4zeOfthZo0hZ7vOAnde', 'Пермь', NULL, 'Ищу работу', NULL, 'photo_2.jpg', '2005-05-17', NULL, NULL, NULL, NULL, NULL);
INSERT INTO engineers."user" VALUES (17, 'Ивалева', 'Софья', NULL, NULL, NULL, 'Пермь', NULL, 'Ищу работу', NULL, 'photo_5.jpg', '2005-05-17', NULL, NULL, NULL, NULL, NULL);


--
-- Data for Name: user_course; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.user_course VALUES (34, 11, 12);
INSERT INTO engineers.user_course VALUES (34, 10, 6);


--
-- Data for Name: user_education; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.user_education VALUES (56, 34, 'ВШЭ');
INSERT INTO engineers.user_education VALUES (57, 34, 'Backend Academy');


--
-- Data for Name: user_section_and_stamp; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.user_section_and_stamp VALUES (14, 1);
INSERT INTO engineers.user_section_and_stamp VALUES (18, 2);
INSERT INTO engineers.user_section_and_stamp VALUES (34, 1);


--
-- Data for Name: user_social_network; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.user_social_network VALUES (58, 34, 'vk.com');
INSERT INTO engineers.user_social_network VALUES (59, 34, 'odnoklassniki.com');


--
-- Data for Name: user_software_skill; Type: TABLE DATA; Schema: engineers; Owner: postgres
--

INSERT INTO engineers.user_software_skill VALUES (14, 1);
INSERT INTO engineers.user_software_skill VALUES (18, 1);
INSERT INTO engineers.user_software_skill VALUES (34, 1);
INSERT INTO engineers.user_software_skill VALUES (34, 3);
INSERT INTO engineers.user_software_skill VALUES (34, 7);


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangelog VALUES ('000', 'you', 'db/changelog/changelog-master.xml', '2025-04-27 23:25:41.384509', 1, 'EXECUTED', '9:c225a0d4c49e68e5845762785a97cf00', 'sql', '', NULL, '4.29.2', NULL, NULL, '5778341359');
INSERT INTO public.databasechangelog VALUES ('001-init-sql', 'you', 'db/changelog/changelog-master.xml', '2025-04-27 23:52:29.291801', 2, 'MARK_RAN', '9:618bd75b68d274a142662a926659d6bd', 'sqlFile path=changeset/01-init.sql', '', NULL, '4.29.2', NULL, NULL, '5779949263');


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangeloglock VALUES (1, false, NULL, NULL);


--
-- Name: collection_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.collection_id_seq', 1, false);


--
-- Name: companies_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.companies_id_seq', 4, true);


--
-- Name: content_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.content_id_seq', 16, true);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.course_id_seq', 11, true);


--
-- Name: lesson_content_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.lesson_content_id_seq', 8, true);


--
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.lesson_id_seq', 12, true);


--
-- Name: module_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.module_id_seq', 6, true);


--
-- Name: notification_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.notification_id_seq', 109, true);


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.password_reset_token_id_seq', 62, true);


--
-- Name: project_content_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.project_content_id_seq', 9, true);


--
-- Name: project_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.project_id_seq', 2, true);


--
-- Name: refresh_token_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.refresh_token_id_seq', 315, true);


--
-- Name: section_and_stamp_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.section_and_stamp_id_seq', 18, true);


--
-- Name: software_skill_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.software_skill_id_seq', 9, true);


--
-- Name: tag_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.tag_id_seq', 3, true);


--
-- Name: user_education_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.user_education_id_seq', 57, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.user_id_seq', 34, true);


--
-- Name: user_social_network_id_seq; Type: SEQUENCE SET; Schema: engineers; Owner: postgres
--

SELECT pg_catalog.setval('engineers.user_social_network_id_seq', 59, true);


--
-- Name: collection collection_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (id);


--
-- Name: companies companies_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);


--
-- Name: content content_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.content
    ADD CONSTRAINT content_pkey PRIMARY KEY (id);


--
-- Name: course course_name_unique; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course
    ADD CONSTRAINT course_name_unique UNIQUE (name);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: course_tag course_tag_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course_tag
    ADD CONSTRAINT course_tag_pkey PRIMARY KEY (course_id, tag_id);


--
-- Name: favourites favourites_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.favourites
    ADD CONSTRAINT favourites_pkey PRIMARY KEY (user_id, project_id);


--
-- Name: homework homework_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.homework
    ADD CONSTRAINT homework_pkey PRIMARY KEY (lesson_id, user_id);


--
-- Name: lesson_content lesson_content_lesson_id_key; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson_content
    ADD CONSTRAINT lesson_content_lesson_id_key UNIQUE (lesson_id);


--
-- Name: lesson_content lesson_content_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson_content
    ADD CONSTRAINT lesson_content_pkey PRIMARY KEY (id);


--
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- Name: module module_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.module
    ADD CONSTRAINT module_pkey PRIMARY KEY (id);


--
-- Name: notification_user notification_user_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.notification_user
    ADD CONSTRAINT notification_user_pkey PRIMARY KEY (user_id, notification_id);


--
-- Name: password_reset_token password_reset_token_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.password_reset_token
    ADD CONSTRAINT password_reset_token_pkey PRIMARY KEY (id);


--
-- Name: password_reset_token password_reset_token_user_id_key; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.password_reset_token
    ADD CONSTRAINT password_reset_token_user_id_key UNIQUE (user_id);


--
-- Name: notification pk_notification; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.notification
    ADD CONSTRAINT pk_notification PRIMARY KEY (id);


--
-- Name: project_content project_content_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_content
    ADD CONSTRAINT project_content_pkey PRIMARY KEY (id);


--
-- Name: project project_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- Name: project_section_and_stamp project_section_and_stamp_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_section_and_stamp
    ADD CONSTRAINT project_section_and_stamp_pkey PRIMARY KEY (project_id, section_and_stamp_id);


--
-- Name: project_software_skill project_software_skill_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_software_skill
    ADD CONSTRAINT project_software_skill_pkey PRIMARY KEY (project_id, software_skill_id);


--
-- Name: project_user_like project_user_like_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_user_like
    ADD CONSTRAINT project_user_like_pkey PRIMARY KEY (project_id, user_id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: refresh_token refresh_token_user_id_key; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.refresh_token
    ADD CONSTRAINT refresh_token_user_id_key UNIQUE (user_id);


--
-- Name: section_and_stamp section_and_stamp_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.section_and_stamp
    ADD CONSTRAINT section_and_stamp_pkey PRIMARY KEY (id);


--
-- Name: software_skill software_skill_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.software_skill
    ADD CONSTRAINT software_skill_pkey PRIMARY KEY (id);


--
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- Name: notification uq_notification_user_proj; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.notification
    ADD CONSTRAINT uq_notification_user_proj UNIQUE (notification_user_id, project_id);


--
-- Name: user_course user_course_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_course
    ADD CONSTRAINT user_course_pkey PRIMARY KEY (user_id, course_id);


--
-- Name: user_education user_education_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_education
    ADD CONSTRAINT user_education_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_section_and_stamp user_section_and_stamp_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_section_and_stamp
    ADD CONSTRAINT user_section_and_stamp_pkey PRIMARY KEY (user_id, section_and_stamp_id);


--
-- Name: user_social_network user_social_network_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_social_network
    ADD CONSTRAINT user_social_network_pkey PRIMARY KEY (id);


--
-- Name: user_software_skill user_software_skill_pkey; Type: CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_software_skill
    ADD CONSTRAINT user_software_skill_pkey PRIMARY KEY (user_id, software_skill_id);


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: content content_lesson_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.content
    ADD CONSTRAINT content_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id);


--
-- Name: course_tag course_tag_course_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course_tag
    ADD CONSTRAINT course_tag_course_id_fkey FOREIGN KEY (course_id) REFERENCES engineers.course(id);


--
-- Name: course_tag course_tag_tag_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.course_tag
    ADD CONSTRAINT course_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES engineers.tag(id);


--
-- Name: favourites favourites_project_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.favourites
    ADD CONSTRAINT favourites_project_id_fkey FOREIGN KEY (project_id) REFERENCES engineers.project(id);


--
-- Name: favourites favourites_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.favourites
    ADD CONSTRAINT favourites_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: collection fk_collection_user; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.collection
    ADD CONSTRAINT fk_collection_user FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: favourites fk_favourites_collection; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.favourites
    ADD CONSTRAINT fk_favourites_collection FOREIGN KEY (collection_id) REFERENCES engineers.collection(id);


--
-- Name: notification_user fk_notification_user_user; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.notification_user
    ADD CONSTRAINT fk_notification_user_user FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: homework homework_lesson_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.homework
    ADD CONSTRAINT homework_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id);


--
-- Name: homework homework_teacher_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.homework
    ADD CONSTRAINT homework_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES engineers."user"(id);


--
-- Name: homework homework_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.homework
    ADD CONSTRAINT homework_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: lesson_content lesson_content_lesson_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson_content
    ADD CONSTRAINT lesson_content_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES engineers.lesson(id);


--
-- Name: lesson lesson_module_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.lesson
    ADD CONSTRAINT lesson_module_id_fkey FOREIGN KEY (module_id) REFERENCES engineers.module(id);


--
-- Name: module module_course_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.module
    ADD CONSTRAINT module_course_id_fkey FOREIGN KEY (course_id) REFERENCES engineers.course(id);


--
-- Name: notification_user notification_user_notification; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.notification_user
    ADD CONSTRAINT notification_user_notification FOREIGN KEY (notification_id) REFERENCES engineers.notification(id);


--
-- Name: password_reset_token password_reset_token_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.password_reset_token
    ADD CONSTRAINT password_reset_token_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: project project_author_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project
    ADD CONSTRAINT project_author_id_fkey FOREIGN KEY (author_id) REFERENCES engineers."user"(id);


--
-- Name: project_content project_content_project_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_content
    ADD CONSTRAINT project_content_project_id_fkey FOREIGN KEY (project_id) REFERENCES engineers.project(id);


--
-- Name: project_section_and_stamp project_section_and_stamp_project_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_section_and_stamp
    ADD CONSTRAINT project_section_and_stamp_project_id_fkey FOREIGN KEY (project_id) REFERENCES engineers.project(id);


--
-- Name: project_section_and_stamp project_section_and_stamp_section_and_stamp_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_section_and_stamp
    ADD CONSTRAINT project_section_and_stamp_section_and_stamp_id_fkey FOREIGN KEY (section_and_stamp_id) REFERENCES engineers.section_and_stamp(id);


--
-- Name: project_software_skill project_software_skill_project_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_software_skill
    ADD CONSTRAINT project_software_skill_project_id_fkey FOREIGN KEY (project_id) REFERENCES engineers.project(id);


--
-- Name: project_software_skill project_software_skill_software_skill_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_software_skill
    ADD CONSTRAINT project_software_skill_software_skill_id_fkey FOREIGN KEY (software_skill_id) REFERENCES engineers.software_skill(id);


--
-- Name: project_user_like project_user_like_project_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_user_like
    ADD CONSTRAINT project_user_like_project_id_fkey FOREIGN KEY (project_id) REFERENCES engineers.project(id);


--
-- Name: project_user_like project_user_like_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.project_user_like
    ADD CONSTRAINT project_user_like_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: refresh_token refresh_token_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.refresh_token
    ADD CONSTRAINT refresh_token_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: user_course user_course_course_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_course
    ADD CONSTRAINT user_course_course_id_fkey FOREIGN KEY (course_id) REFERENCES engineers.course(id);


--
-- Name: user_course user_course_last_lesson_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_course
    ADD CONSTRAINT user_course_last_lesson_id_fkey FOREIGN KEY (last_lesson_id) REFERENCES engineers.lesson(id);


--
-- Name: user_course user_course_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_course
    ADD CONSTRAINT user_course_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: user_education user_education_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_education
    ADD CONSTRAINT user_education_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: user_section_and_stamp user_section_and_stamp_section_and_stamp_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_section_and_stamp
    ADD CONSTRAINT user_section_and_stamp_section_and_stamp_id_fkey FOREIGN KEY (section_and_stamp_id) REFERENCES engineers.section_and_stamp(id);


--
-- Name: user_section_and_stamp user_section_and_stamp_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_section_and_stamp
    ADD CONSTRAINT user_section_and_stamp_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: user_social_network user_social_network_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_social_network
    ADD CONSTRAINT user_social_network_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: user_software_skill user_software_skill_software_skill_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_software_skill
    ADD CONSTRAINT user_software_skill_software_skill_id_fkey FOREIGN KEY (software_skill_id) REFERENCES engineers.software_skill(id);


--
-- Name: user_software_skill user_software_skill_user_id_fkey; Type: FK CONSTRAINT; Schema: engineers; Owner: postgres
--

ALTER TABLE ONLY engineers.user_software_skill
    ADD CONSTRAINT user_software_skill_user_id_fkey FOREIGN KEY (user_id) REFERENCES engineers."user"(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;


--
-- PostgreSQL database dump complete
--


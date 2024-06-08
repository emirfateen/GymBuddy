--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- Name: MUSCLES; Type: TYPE; Schema: public; Owner: emir
--

CREATE TYPE public."MUSCLES" AS ENUM (
    'ABS',
    'TRICEP',
    'BICEP',
    'BACK',
    'CHEST',
    'LEG'
);


ALTER TYPE public."MUSCLES" OWNER TO emir;

--
-- Name: day_of_week; Type: TYPE; Schema: public; Owner: emir
--

CREATE TYPE public.day_of_week AS ENUM (
    'Sunday',
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday'
);


ALTER TYPE public.day_of_week OWNER TO emir;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Exercise; Type: TABLE; Schema: public; Owner: emir
--

CREATE TABLE public."Exercise" (
    exeid uuid NOT NULL,
    name character varying(255),
    muscle public."MUSCLES"
);


ALTER TABLE public."Exercise" OWNER TO emir;

--
-- Name: Reminder; Type: TABLE; Schema: public; Owner: emir
--

CREATE TABLE public."Reminder" (
    remid uuid NOT NULL,
    userid uuid,
    remname text,
    reminder_time public.day_of_week
);


ALTER TABLE public."Reminder" OWNER TO emir;

--
-- Name: Routine; Type: TABLE; Schema: public; Owner: emir
--

CREATE TABLE public."Routine" (
    rouid uuid NOT NULL,
    userid uuid,
    rouname character varying(255),
    description text
);


ALTER TABLE public."Routine" OWNER TO emir;

--
-- Name: User; Type: TABLE; Schema: public; Owner: emir
--

CREATE TABLE public."User" (
    userid uuid NOT NULL,
    username character varying(255),
    email character varying(255),
    password character varying(255)
);


ALTER TABLE public."User" OWNER TO emir;

--
-- Name: Variation; Type: TABLE; Schema: public; Owner: emir
--

CREATE TABLE public."Variation" (
    varid uuid NOT NULL,
    exeid uuid,
    rouid uuid,
    sets integer,
    reps integer
);


ALTER TABLE public."Variation" OWNER TO emir;

--
-- Data for Name: Exercise; Type: TABLE DATA; Schema: public; Owner: emir
--

COPY public."Exercise" (exeid, name, muscle) FROM stdin;
ac9799e4-d260-4b4f-811b-bf8ee1c92a82	Lat Pulldown	BACK
938edd53-bed9-43e6-bea8-38516f0f67b6	Chin Up	BACK
178aac82-173f-41f1-92b1-d71bd236b592	Incline Row	BACK
4d6c4fa6-0b83-4eba-b442-a0e0073232e0	Pull Up	BACK
60247ff0-7b55-437e-b794-fda941aa2160	Shrug	BACK
fed33713-b04f-4654-804c-4ffbfbf0d9f4	Muscle Up	BACK
04f0e7a3-9bd1-4e64-ab02-aef7df754623	Plank	ABS
175f6cbe-85e2-4eb9-a8b9-7b9d211928ea	Sit-up	ABS
9b1e87c3-122c-4399-97d4-5c617be6a23f	Russian Twist	ABS
7effae21-0b5a-4236-a65e-989e6ae0eeb6	Push-up	CHEST
a8dc2da8-1713-4f26-b9c3-fd0eb1467253	Pec Fly	CHEST
f8594247-a411-4b25-8440-0a57b0cfa93b	Bench Press	CHEST
5afc1325-59a5-4f2f-b81e-bedfc016368b	Chest Fly	CHEST
fd80782d-e85a-4e55-a7a3-5083ff8e0da5	Chest Dip	CHEST
645f00db-3ee8-43a8-8a12-5f4849087dea	Squat	LEG
1231ddd6-1d97-4a5c-828c-268dc8e0e01c	Leg Curl	LEG
d77e6a8b-7881-4d10-a558-faa849df91eb	Deadlift	LEG
e5ad0141-48ba-4c3b-99cc-7caf502512bc	Hip Thrust	LEG
fe9b85e4-0768-4565-bf41-41116ed0c869	Lunge	LEG
cc8c3502-9ddd-438f-a72d-8eafc8eda6a4	Calf Raise	LEG
\.


--
-- Data for Name: Reminder; Type: TABLE DATA; Schema: public; Owner: emir
--

COPY public."Reminder" (remid, userid, remname, reminder_time) FROM stdin;
1547d95e-0049-4d52-8034-bd7f4b4bc002	5d4623e4-8476-4c2c-a168-7e3626db0f68	apai	Wednesday
\.


--
-- Data for Name: Routine; Type: TABLE DATA; Schema: public; Owner: emir
--

COPY public."Routine" (rouid, userid, rouname, description) FROM stdin;
c6d5bdd5-7f9d-49cf-8cc3-cefe114c833e	35284ce7-aa08-4a5a-a11e-0ceefebc8597	Chest Day	Get that chest pumped
\.


--
-- Data for Name: User; Type: TABLE DATA; Schema: public; Owner: emir
--

COPY public."User" (userid, username, email, password) FROM stdin;
38ef31ba-540c-4f75-ad01-6d23a4498745	emirftn	emir@gmail.com	$2b$10$LCFzhbuvb5Sfb29BYU2bU.8vEuWaxY5pfGXyPvXylLgLCmG2/HZpC
35284ce7-aa08-4a5a-a11e-0ceefebc8597	sha	fenestraz@gmail.com	$2b$10$r0ka82XYoQ80.fZay2xct.GpW5kjnn2f5H.MUKUsV0h2oTAnO/Oei
6dd41a00-28b7-482c-881c-d828f4abc61f	suryads	suryads@gmail.com	$2b$10$NSUEl2iwb8/9dWS.heIvaO4oLJO1.UMA.uoRZEo6iEwba5uWnSH6e
5d4623e4-8476-4c2c-a168-7e3626db0f68	hazera	hazera@valorant.co.id	$2b$10$0sbZIbQO26tlMHz6fdpa5.xepeNOHVbBNPofb8FWnO1qS/Bc120hq
3b5e23ff-30b9-4476-acc5-136434579f9c	kamal	kamal@email.com	$2b$10$4V/0FfF4kimFJATz1Lb12unQZ32nB1lNfALmodPN.yqYEk3QmJ1GC
3517325d-55e2-4fc1-b7ff-79495e3aa859	kamal	makarim@gmail.com	$2b$10$mZiZpihZIwWWvcXg.gljh.R9/iHKscAh0B36kZ.Eo5t/8hfv6sJTu
\.


--
-- Data for Name: Variation; Type: TABLE DATA; Schema: public; Owner: emir
--

COPY public."Variation" (varid, exeid, rouid, sets, reps) FROM stdin;
83f82e19-df3e-4f04-9268-5515ffee1fc4	a8dc2da8-1713-4f26-b9c3-fd0eb1467253	c6d5bdd5-7f9d-49cf-8cc3-cefe114c833e	3	10
\.


--
-- Name: Exercise Exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Exercise"
    ADD CONSTRAINT "Exercise_pkey" PRIMARY KEY (exeid);


--
-- Name: Reminder Reminder_pkey; Type: CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Reminder"
    ADD CONSTRAINT "Reminder_pkey" PRIMARY KEY (remid);


--
-- Name: Routine Routine_pkey; Type: CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Routine"
    ADD CONSTRAINT "Routine_pkey" PRIMARY KEY (rouid);


--
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (userid);


--
-- Name: Variation Variation_pkey; Type: CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Variation"
    ADD CONSTRAINT "Variation_pkey" PRIMARY KEY (varid);


--
-- Name: Routine Routine_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Routine"
    ADD CONSTRAINT "Routine_userid_fkey" FOREIGN KEY (userid) REFERENCES public."User"(userid);


--
-- Name: Variation Variation_exeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Variation"
    ADD CONSTRAINT "Variation_exeid_fkey" FOREIGN KEY (exeid) REFERENCES public."Exercise"(exeid);


--
-- Name: Variation Variation_rouid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: emir
--

ALTER TABLE ONLY public."Variation"
    ADD CONSTRAINT "Variation_rouid_fkey" FOREIGN KEY (rouid) REFERENCES public."Routine"(rouid);


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO neon_superuser WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON TABLES TO neon_superuser WITH GRANT OPTION;


--
-- PostgreSQL database dump complete
--


CREATE TYPE "MUSCLES" AS ENUM (
  'ABS',
  'BACK',
  'CHEST',
  'LEG'
);

CREATE TYPE day_of_week AS ENUM (\
  'Sunday', 
  'Monday', 
  'Tuesday', 
  'Wednesday', 
  'Thursday', 
  'Friday', 
  'Saturday'
);


CREATE TABLE "User" (
  "userid" uuid UNIQUE PRIMARY KEY,
  "username" varchar(255),
  "email" varchar(255),
  "password" varchar(255)
);

CREATE TABLE "Routine" (
  "rouid" uuid PRIMARY KEY,
  "userid" uuid,
  "rouname" VARCHAR(255),
  "description" text
);

CREATE TABLE "Reminder" (
  "remid" uuid PRIMARY KEY,
  "userid" uuid,
  "remname" text,
  "reminder_time" day_of_week
);

CREATE TABLE "Exercise" (
  "exeid" uuid PRIMARY KEY,
  "name" varchar(255),
  "muscle" "MUSCLES"
);

CREATE TABLE "Variation" (
  "varid" uuid PRIMARY KEY,
  "exeid" uuid,
  "rouid" uuid,
  "sets" integer,
  "reps" integer
);

ALTER TABLE "Routine" ADD FOREIGN KEY ("userid") REFERENCES "User" ("userid");

ALTER TABLE "Reminder" ADD FOREIGN KEY ("userid") REFERENCES "User" ("userid");

ALTER TABLE "Variation" ADD FOREIGN KEY ("exeid") REFERENCES "Exercise" ("exeid");

ALTER TABLE "Variation" ADD FOREIGN KEY ("rouid") REFERENCES "Routine" ("rouid");


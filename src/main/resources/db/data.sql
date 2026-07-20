-- Insert ICON_COLOR records
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 0, 0);   -- Red
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 255, 0);   -- Green
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 0, 255);   -- Blue
INSERT INTO ICON_COLOR (red, green, blue) VALUES (128, 0, 128); -- Purple
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 165, 0); -- Orange

-- Insert TASK_STATUS records
INSERT INTO TASK_STATUS (statusName) VALUES ('ACTIVE');
INSERT INTO TASK_STATUS (statusName) VALUES ('COMPLETED');
INSERT INTO TASK_STATUS (statusName) VALUES ('PAST');

-- Insert TAGS records
INSERT INTO TAG (tagName, iconColorId) VALUES ('Urgent', 1);
INSERT INTO TAG (tagName, iconColorId) VALUES ('Home', 2);
INSERT INTO TAG (tagName, iconColorId) VALUES ('Gym', 3);
INSERT INTO TAG (tagName, iconColorId) VALUES ('Coding', 4);

-- Insert WEEK_DAYS records
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (1, 'MONDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (2, 'TUESDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (3, 'WEDNESDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (4, 'THURSDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (5, 'FRIDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (6, 'SATURDAY');
INSERT INTO WEEK_DAYS (weekDayId, dayName) VALUES (7, 'SUNDAY');

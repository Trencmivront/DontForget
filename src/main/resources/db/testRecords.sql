-- Insert ICON_COLOR records
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 0, 0);   -- Red
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 255, 0);   -- Green
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 0, 255);   -- Blue
INSERT INTO ICON_COLOR (red, green, blue) VALUES (128, 0, 128); -- Purple
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 165, 0); -- Orange

-- Insert TASK_STATUS records
INSERT INTO TASK_STATUS (status_name) VALUES ('ACTIVE');
INSERT INTO TASK_STATUS (status_name) VALUES ('COMPLETED');
INSERT INTO TASK_STATUS (status_name) VALUES ('PAST');

-- Insert TAGS records
INSERT INTO TAG (tag_name, icon_color_id) VALUES ('Urgent', 1);
INSERT INTO TAG (tag_name, icon_color_id) VALUES ('Home', 2);
INSERT INTO TAG (tag_name, icon_color_id) VALUES ('Gym', 3);
INSERT INTO TAG (tag_name, icon_color_id) VALUES ('Coding', 4);

-- Insert WEEK_DAYS records
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (1, 'MONDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (2, 'TUESDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (3, 'WEDNESDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (4, 'THURSDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (5, 'FRIDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (6, 'SATURDAY');
INSERT INTO WEEK_DAYS (week_day_id, day_name) VALUES (7, 'SUNDAY');

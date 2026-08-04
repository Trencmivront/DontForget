-- Insert ICON_COLOR records
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 0, 0);     -- Red
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 200, 83);    -- Green
INSERT INTO ICON_COLOR (red, green, blue) VALUES (33, 150, 243);  -- Blue
INSERT INTO ICON_COLOR (red, green, blue) VALUES (156, 39, 176);  -- Purple
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 152, 0);   -- Orange
INSERT INTO ICON_COLOR (red, green, blue) VALUES (244, 67, 54);   -- Coral Red
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 188, 212);   -- Cyan
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 235, 59);  -- Yellow
INSERT INTO ICON_COLOR (red, green, blue) VALUES (233, 30, 99);   -- Pink
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 150, 136);   -- Teal
INSERT INTO ICON_COLOR (red, green, blue) VALUES (103, 58, 183);  -- Deep Purple
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 87, 34);   -- Deep Orange
INSERT INTO ICON_COLOR (red, green, blue) VALUES (96, 125, 139);  -- Blue Grey
INSERT INTO ICON_COLOR (red, green, blue) VALUES (121, 85, 72);   -- Brown
INSERT INTO ICON_COLOR (red, green, blue) VALUES (76, 175, 80);   -- Light Green
INSERT INTO ICON_COLOR (red, green, blue) VALUES (63, 81, 181);   -- Indigo
INSERT INTO ICON_COLOR (red, green, blue) VALUES (0, 96, 100);    -- Dark Teal
INSERT INTO ICON_COLOR (red, green, blue) VALUES (255, 193, 7);   -- Amber
INSERT INTO ICON_COLOR (red, green, blue) VALUES (171, 71, 188);  -- Orchid
INSERT INTO ICON_COLOR (red, green, blue) VALUES (38, 166, 154);  -- Medium Aquamarine
INSERT INTO ICON_COLOR (red, green, blue) VALUES (239, 108, 0);   -- Dark Orange
INSERT INTO ICON_COLOR (red, green, blue) VALUES (30, 136, 229);  -- Dodger Blue
INSERT INTO ICON_COLOR (red, green, blue) VALUES (216, 27, 96);   -- Crimson
INSERT INTO ICON_COLOR (red, green, blue) VALUES (85, 139, 47);   -- Olive Green
INSERT INTO ICON_COLOR (red, green, blue) VALUES (69, 90, 100);   -- Slate

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

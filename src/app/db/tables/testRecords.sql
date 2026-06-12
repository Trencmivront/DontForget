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

-- Insert PROJECTS records
INSERT INTO PROJECTS (project_title, description, list_order, icon_color_id) VALUES ('Work Tasks', 'Must be done today.', 1, 1);
INSERT INTO PROJECTS (project_title, description, list_order, icon_color_id) VALUES ('Personal Tasks', 'I can do them when I want.', 2, 2);
INSERT INTO PROJECTS (project_title, description, list_order, icon_color_id) VALUES ('Fitness & Health', 'Pluh.', 3, 3);

-- Insert TAGS records
INSERT INTO TAGS (tag_name, icon_color_id) VALUES ('Urgent', 1);
INSERT INTO TAGS (tag_name, icon_color_id) VALUES ('Home', 2);
INSERT INTO TAGS (tag_name, icon_color_id) VALUES ('Gym', 3);
INSERT INTO TAGS (tag_name, icon_color_id) VALUES ('Coding', 4);

-- Insert TASKS records
-- Note: due_date must be >= CURRENT_DATE
INSERT INTO TASKS (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at, completed_at) 
VALUES ('Finish Design Document', 'Write and share the design document for the new module.', 1, 1, CURRENT_DATE, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO TASKS (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at, completed_at) 
VALUES ('Buy Weekly Groceries', 'Get vegetables, milk, and eggs.', 1, 2, DATEADD('DAY', 2, CURRENT_DATE), 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO TASKS (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at, completed_at) 
VALUES ('Run 5 Kilometers', 'Daily morning cardio workout.', 2, 3, CURRENT_DATE, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO TASKS (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at, completed_at) 
VALUES ('Fix Bug in Login API', 'Resolve session timeout issue reported in Jira.', 1, 1, DATEADD('DAY', 5, CURRENT_DATE), 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- Insert RECURRING_TASKS records (1:1 relation with TASKS)
INSERT INTO RECURRING_TASKS (task_id, day_of_week, max_occourrences) VALUES (3, 'MONDAY', 12);

-- Insert REMINDERS records (1:1 relation with TASKS)
INSERT INTO REMINDERS (task_id, remind_at, cstm_message) VALUES (1, DATEADD('HOUR', 4, CURRENT_TIMESTAMP), 'Please review the design document with the tech lead.');
INSERT INTO REMINDERS (task_id, remind_at, cstm_message) VALUES (2, DATEADD('DAY', 1, CURRENT_TIMESTAMP), 'Remember to bring the shopping bags!');

-- Insert TASK_TAGS records (M:N relation)
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (1, 1); -- 'Finish Design Document' -> 'Urgent'
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (1, 4); -- 'Finish Design Document' -> 'Coding'
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (2, 2); -- 'Buy Weekly Groceries' -> 'Home'
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (3, 3); -- 'Run 5 Kilometers' -> 'Gym'
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (4, 1); -- 'Fix Bug in Login API' -> 'Urgent'
INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (4, 4); -- 'Fix Bug in Login API' -> 'Coding'

-- Insert INBOX records
INSERT INTO INBOX (message, created_at) VALUES ('Welcome to your Inbox! Keep track of all system updates and quick notes here.', CURRENT_TIMESTAMP);
INSERT INTO INBOX (message, created_at) VALUES ('New reminder set for "Finish Design Document".', CURRENT_TIMESTAMP);

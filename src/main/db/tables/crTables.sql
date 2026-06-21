-- Create ICON_COLOR table
CREATE TABLE IF NOT EXISTS ICON_COLOR (
    icon_color_id INT AUTO_INCREMENT PRIMARY KEY,
    red INT NOT NULL CHECK (red BETWEEN 0 AND 256),
    green INT NOT NULL CHECK (green BETWEEN 0 AND 256),
    blue INT NOT NULL CHECK (blue BETWEEN 0 AND 256)
);

-- Create TASK_STATUS table
CREATE TABLE IF NOT EXISTS TASK_STATUS (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE CHECK (status_name IN ('ACTIVE', 'COMPLETED', 'PAST'))
);

-- Create PROJECTS table
CREATE TABLE IF NOT EXISTS PROJECT (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_title VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    list_order INT,
    icon_color_id INT NOT NULL,
    FOREIGN KEY (icon_color_id) REFERENCES ICON_COLOR(icon_color_id)
);

-- Create TAGS table
CREATE TABLE IF NOT EXISTS TAG (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(255) NOT NULL UNIQUE,
    icon_color_id INT NOT NULL,
    FOREIGN KEY (icon_color_id) REFERENCES ICON_COLOR(icon_color_id)
);

-- Create TASKS table
CREATE TABLE IF NOT EXISTS TASK (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    task_title VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    status_id INT,
    priority INT CHECK (priority IN (1, 2, 3)),
    due_date DATE CHECK (due_date >= CURRENT_DATE),
    list_order INT,
    project_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES TASK_STATUS(status_id),
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id)
);

-- Create RECURRING_TASKS table (1:1 relation with TASKS)
CREATE TABLE IF NOT EXISTS RECURRING_TASK (
    task_id INT PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    max_occourrences INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES TASK(task_id)
);

-- Create REMINDERS table (1:1 relation with TASKS)
CREATE TABLE IF NOT EXISTS REMINDER (
    task_id INT PRIMARY KEY,
    remind_at TIMESTAMP NOT NULL,
    cstm_message VARCHAR(1000),
    FOREIGN KEY (task_id) REFERENCES TASK(task_id)
);

-- Create TASK_TAGS junction table (M:N relation between TASKS and TAGS)
CREATE TABLE IF NOT EXISTS TASK_TAG (
    task_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (task_id, tag_id),
    FOREIGN KEY (task_id) REFERENCES TASK(task_id),
    FOREIGN KEY (tag_id) REFERENCES TAG(tag_id)
);

-- Create INBOX table
CREATE TABLE IF NOT EXISTS INBOX (
    inbox_id INT AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

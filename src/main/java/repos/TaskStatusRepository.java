package main.java.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.entities.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}

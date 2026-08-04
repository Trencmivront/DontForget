package main.io.github.trencmivront.dontforget.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.io.github.trencmivront.dontforget.entities.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}

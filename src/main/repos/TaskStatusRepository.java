package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import main.entities.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}

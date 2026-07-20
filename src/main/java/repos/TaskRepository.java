package main.java.repos;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.java.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByprojectId(Long projectId);

    @Query("SELECT COALESCE(MAX(t.listOrder), 0) FROM Task t WHERE t.projectId = :projectId")
    Integer findMaxListOrderByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM Task t WHERE CAST(t.dueDate AS date) = CAST(CURRENT_TIMESTAMP AS date)")
    List<Task> findTodaysTasks();

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end")
    List<Task> findByDueDateBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);

    List<Task> findBystatusId(Long statusId);
}

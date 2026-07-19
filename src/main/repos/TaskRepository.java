package main.repos;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import main.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject_id(Long projectId);

    @Query("SELECT COALESCE(MAX(t.list_order), 0) FROM Task t WHERE t.project_id = :projectId")
    Integer findMaxListOrderByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM Task t WHERE CAST(t.due_date AS date) = CAST(CURRENT_TIMESTAMP AS date)")
    List<Task> findTodaysTasks();

    @Query("SELECT t FROM Task t WHERE t.due_date BETWEEN :start AND :end")
    List<Task> findByDueDateBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);

    List<Task> findByStatus_id(Long statusId);
}

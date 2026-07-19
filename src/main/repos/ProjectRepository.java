package main.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import main.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOrderByListOrderAsc();

    @Query("SELECT p FROM Project p JOIN Task t ON t.project_id = p.project_id WHERE t.task_id = :taskId")
    Project findByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT COALESCE(MAX(p.listOrder), 0) FROM Project p")
    Integer findMaxListOrder();
}

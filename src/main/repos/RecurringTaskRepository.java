package main.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import main.entities.RecurringTask;

public interface RecurringTaskRepository extends JpaRepository<RecurringTask, RecurringTask.RecurringTaskId> {

    List<RecurringTask> findByTask_id(Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecurringTask rt WHERE rt.task_id = :taskId")
    void deleteByTask_id(@Param("taskId") Long taskId);
}

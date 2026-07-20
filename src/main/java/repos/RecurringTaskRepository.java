package main.java.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import main.java.entities.RecurringTask;

public interface RecurringTaskRepository extends JpaRepository<RecurringTask, RecurringTask.RecurringTaskId> {

    List<RecurringTask> findBytaskId(Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecurringTask rt WHERE rt.taskId = :taskId")
    void deleteBytaskId(@Param("taskId") Long taskId);
}

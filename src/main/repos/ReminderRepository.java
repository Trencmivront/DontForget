package main.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import main.entities.Reminder;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Reminder r WHERE r.task_id = :taskId")
    void deleteByTask_id(@Param("taskId") Long taskId);

    @Query("SELECT r FROM Reminder r ORDER BY r.remind_at ASC")
    List<Reminder> findAllOrderByRemindAtAsc();
}

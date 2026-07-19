package main.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import main.entities.TaskTag;

public interface TaskTagRepository extends JpaRepository<TaskTag, TaskTag.TaskTagId> {

    List<TaskTag> findByTask_id(Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskTag tt WHERE tt.task_id = :taskId")
    void deleteByTask_id(@Param("taskId") Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskTag tt WHERE tt.tag_id = :tagId")
    void deleteByTag_id(@Param("tagId") Long tagId);
}

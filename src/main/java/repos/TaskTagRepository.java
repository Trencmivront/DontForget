package main.java.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import main.java.entities.TaskTag;

public interface TaskTagRepository extends JpaRepository<TaskTag, TaskTag.TaskTagId> {

    List<TaskTag> findBytaskId(Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskTag tt WHERE tt.taskId = :taskId")
    void deleteBytaskId(@Param("taskId") Long taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskTag tt WHERE tt.tagId = :tagId")
    void deleteBytagId(@Param("tagId") Long tagId);
}

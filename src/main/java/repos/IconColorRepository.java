package main.java.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.java.entities.IconColor;

public interface IconColorRepository extends JpaRepository<IconColor, Long> {

    @Query("SELECT ic FROM IconColor ic JOIN Project p ON p.iconColorId = ic.iconColorId WHERE p.projectId = :projectId")
    IconColor findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT ic FROM IconColor ic JOIN Tag t ON t.iconColorId = ic.iconColorId WHERE t.tagId = :tagId")
    IconColor findByTagId(@Param("tagId") Long tagId);
}

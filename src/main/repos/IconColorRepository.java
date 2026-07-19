package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import main.entities.IconColor;

public interface IconColorRepository extends JpaRepository<IconColor, Long> {

    @Query("SELECT ic FROM IconColor ic JOIN Project p ON p.icon_color_id = ic.icon_color_id WHERE p.project_id = :projectId")
    IconColor findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT ic FROM IconColor ic JOIN Tag t ON t.icon_color_id = ic.icon_color_id WHERE t.tag_id = :tagId")
    IconColor findByTagId(@Param("tagId") Long tagId);
}

package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import main.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

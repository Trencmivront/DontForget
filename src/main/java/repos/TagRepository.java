package main.java.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

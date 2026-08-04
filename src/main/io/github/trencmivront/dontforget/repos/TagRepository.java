package main.io.github.trencmivront.dontforget.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.io.github.trencmivront.dontforget.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

package main.io.github.trencmivront.dontforget.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.io.github.trencmivront.dontforget.entities.Inbox;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
}

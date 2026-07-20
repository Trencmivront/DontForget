package main.java.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.entities.Inbox;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
}

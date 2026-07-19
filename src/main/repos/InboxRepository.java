package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import main.entities.Inbox;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
}

package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import main.entities.WeekDays;

public interface WeekDaysRepository extends JpaRepository<WeekDays, Long> {
}

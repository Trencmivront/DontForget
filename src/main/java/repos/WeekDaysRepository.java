package main.java.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.entities.WeekDays;

public interface WeekDaysRepository extends JpaRepository<WeekDays, Long> {
}

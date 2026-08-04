package main.io.github.trencmivront.dontforget.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.io.github.trencmivront.dontforget.entities.WeekDays;

public interface WeekDaysRepository extends JpaRepository<WeekDays, Long> {
}

package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}

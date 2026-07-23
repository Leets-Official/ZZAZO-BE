package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.TimetableLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableLectureRepository extends JpaRepository<TimetableLecture, Long> {
}

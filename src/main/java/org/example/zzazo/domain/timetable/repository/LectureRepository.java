package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}

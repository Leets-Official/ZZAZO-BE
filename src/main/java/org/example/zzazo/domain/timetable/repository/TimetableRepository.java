package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findAllByUser_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    Optional<Timetable> findByTimetableIdAndDeletedAtIsNull(Long timetableId);
}

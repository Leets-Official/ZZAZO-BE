package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.TimetableLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimetableLectureRepository extends JpaRepository<TimetableLecture, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from TimetableLecture tl where tl.timetable.timetableId = :timetableId")
    void deleteAllByTimetableId(@Param("timetableId") Long timetableId);
}

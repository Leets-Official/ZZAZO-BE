package org.example.zzazo.domain.timetable.repository;

import org.example.zzazo.domain.timetable.entity.TimetableLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimetableLectureRepository extends JpaRepository<TimetableLecture, Long> {

    @Query("select distinct tl from TimetableLecture tl " +
            "join fetch tl.lecture l " +
            "left join fetch l.lectureSchedules " +
            "where tl.timetable.timetableId = :timetableId " +
            "order by tl.timetableLectureId")
    List<TimetableLecture> findAllWithLectureAndSchedulesByTimetableId(
            @Param("timetableId") Long timetableId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from TimetableLecture tl where tl.timetable.timetableId = :timetableId")
    void deleteAllByTimetableId(@Param("timetableId") Long timetableId);
}

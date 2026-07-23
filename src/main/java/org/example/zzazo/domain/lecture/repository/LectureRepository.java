package org.example.zzazo.domain.lecture.repository;

import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    //교양 과목목록 조회
    @EntityGraph(attributePaths = {"lectureSchedules"})
    List<Lecture> findAllByLiberalCategoryAndSemester(LiberalCategory liberalCategory,int semester);

    //id로 과목목록 조회
    @Query("select distinct l from Lecture l left join fetch l.lectureSchedules where l.id in :ids")
    List<Lecture> findAllByIdInWithSchedules(@Param("ids") List<Long> ids);
}
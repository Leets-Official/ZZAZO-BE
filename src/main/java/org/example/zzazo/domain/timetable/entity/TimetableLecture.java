package org.example.zzazo.domain.timetable.entity;

import jakarta.persistence.*;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.global.entity.BaseTimeEntity;

@Entity
@Table(name = "timetable_lecture")
public class TimetableLecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_lecture_id")
    private Long timetableLectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id", nullable = false)
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    protected TimetableLecture() {
    }

    private TimetableLecture(Timetable timetable, Lecture lecture) {
        this.timetable = timetable;
        this.lecture = lecture;
    }

    public static TimetableLecture of(Timetable timetable, Lecture lecture) {
        return new TimetableLecture(timetable, lecture);
    }
}

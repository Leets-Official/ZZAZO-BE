package org.example.zzazo.domain.recommend.domain;

import lombok.Getter;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.domain.recommend.exception.RecommendErrorCode;
import org.example.zzazo.global.common.Week;
import org.example.zzazo.global.error.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class SelectedTimetable {
    private static final int MAXIMUM_CREDIT = 30;
    private final List<Lecture> lectures = new ArrayList<>();

    public void add(Lecture lecture) {
        if (isExceedMaxCredit(lecture)) {
            throw new CustomException(RecommendErrorCode.EXCEED_THIRTY);
        }
        if (hasTimeConflict(lecture)) {
            throw new CustomException(RecommendErrorCode.SCHEDULE_OVERLAPPED);
        }
        if (hasSameLectureGroup(lecture)) {
            throw new CustomException(RecommendErrorCode.SELECT_DUPLICATED);
        }
        this.lectures.add(lecture);
    }

    public boolean canAdd(Lecture lecture) {
        return !isExceedMaxCredit(lecture)
                && !hasTimeConflict(lecture)
                && !hasSameLectureGroup(lecture);
    }

    public int getTotalCredit() {
        return lectures.stream().mapToInt(Lecture::getCredit).sum();
    }


    public Set<Week> getUsedDays() {
        return lectures.stream().flatMap(l -> l.getLectureSchedules().stream())
                .map(LectureSchedule::getDayOfWeek)
                .collect(Collectors.toSet());
    }

    private boolean isExceedMaxCredit(Lecture lecture) {
        return getTotalCredit() + lecture.getCredit() > MAXIMUM_CREDIT;
    }

    private boolean hasSameLectureGroup(Lecture candidate) {
        return lectures.stream()
                .anyMatch(l -> l.getLectureGroup().getId().equals(candidate.getLectureGroup().getId()));
    }

    private boolean hasTimeConflict(Lecture candidate) {
        return lectures.stream()
                .anyMatch(l -> l.isOverlapWith(candidate));
    }

}
package org.example.zzazo.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.curriculum.repository.CurriculumRepository;
import org.example.zzazo.domain.department.repository.DepartmentRepository;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lecture.repository.LectureRepository;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.dto.RecommendResponse;
import org.example.zzazo.domain.recommend.exception.RecommendErrorCode;
import org.example.zzazo.domain.recommend.strategy.RecommendStrategy;
import org.example.zzazo.global.common.Week;
import org.example.zzazo.global.error.CustomException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final CurriculumRepository curriculumRepository;
    private final DepartmentRepository departmentRepository;
    private final List<RecommendStrategy> strategies;
    private final LectureRepository lectureRepository;

    public RecommendResponse.RecommendResult recommendTimeTable(RecommendRequest.createRecommendRequest request) {
        // 학과 검증
        departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new CustomException(RecommendErrorCode.DEPARTMENT_NOT_EXISTS));

        // 데이터 조회
        List<Curriculum> curriculums = getCurriculums(
                request.departmentId(),
                request.semester(),
                request.preferredFreeDays()
        );

        List<Lecture> selected = getSelectedLectures(
                request.selectedLectureIds(),
                request.semester(),
                request.preferredFreeDays()
        );


        // 시간표 객체 생성 및 필수 과목 기본 세팅
        SelectedTimetable timeTable = new SelectedTimetable();
        selected.forEach(timeTable::add);

        // 전략 패턴을 통한 시간표 알고리즘 실행
        RecommendStrategy strategy = findStrategy(request.priority());
        SelectedTimetable completedTimeTable = strategy.generate(curriculums, timeTable, request);

        // 목표 학점 달성 검증
        if (completedTimeTable.getTotalCredit() < request.targetCredits()) {
            throw new CustomException(RecommendErrorCode.RECOMMEND_NOT_EXISTS);
        }

        return RecommendResponse.RecommendResult.of(completedTimeTable, request.departmentId(),request.preferredFreeDays());
    }

    private RecommendStrategy findStrategy(Priority priority) {
        return strategies.stream()
                .filter(strategies-> strategies.supports(priority))
                .findFirst().orElseThrow();
    }

    private List<Curriculum> getCurriculums(Long departmentId,int semester,List<Week> preferredFreeDays) {
        if (preferredFreeDays == null || preferredFreeDays.isEmpty()) {
            return curriculumRepository.findCurriculumsByDepartmentIdAndSemester(
                    departmentId, semester);
        }
        return curriculumRepository.findCurriculumsByDepartmentIdAndSemesterExcludingFreeDays(
                departmentId, semester, preferredFreeDays);
    }

    private List<Lecture> getSelectedLectures(
            List<Long> selectedLectureIds,
            int semester,
            List<Week> preferredFreeDays
    ) {


        // 선택한 필수 강의가 없는 경우 빠른 반환
        if (selectedLectureIds == null || selectedLectureIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 요청 ID 목록 내 중복 선택 검증
        if (selectedLectureIds.stream().distinct().count() != selectedLectureIds.size()) {
            throw new CustomException(RecommendErrorCode.SELECT_DUPLICATED);
        }

        // DB에서 강의 및 강의 시간 조회
        Map<Long, Lecture> lectureById = lectureRepository.findAllByIdInWithSchedules(selectedLectureIds)
                .stream()
                .collect(Collectors.toMap(Lecture::getId, l -> l));

        List<Lecture> selected = new ArrayList<>();

        // 4. 각 필수 과목별 비즈니스 규칙 검증
        for (Long lectureId : selectedLectureIds) {
            Lecture lecture = lectureById.get(lectureId);

            // DB에 존재하지 않는 강의 ID
            if (lecture == null) {
                throw new CustomException(RecommendErrorCode.LECTURE_NOT_EXISTS);
            }

            // 요청한 학기와 강의의 학기가 불일치
            if (lecture.getSemester() != semester) {
                throw new CustomException(RecommendErrorCode.SEMESTER_NOT_EQUALS);
            }

            // 사용자가 지정한 선호 공강 요일 과목
            if (hasScheduleOnFreeDays(lecture, preferredFreeDays)) {
                throw new CustomException(RecommendErrorCode.SELECT_FREE_DAYS);
            }

            selected.add(lecture);
        }

        return selected;
    }

    private boolean hasScheduleOnFreeDays(Lecture lecture, List<Week> preferredFreeDays) {
        if (preferredFreeDays == null || preferredFreeDays.isEmpty()) {
            return false;
        }
        return lecture.getLectureSchedules().stream()
                .map(LectureSchedule::getDayOfWeek)
                .anyMatch(preferredFreeDays::contains);
    }
}
package org.example.zzazo.domain.recommend.strategy;

import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.exception.RecommendErrorCode;
import org.example.zzazo.global.common.Week;
import org.example.zzazo.global.error.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FreePeriodStrategy implements RecommendStrategy {

    @Value("${recommend.timeout}")
    private long recommendExpired;

    @Override
    public boolean supports(Priority priority) {
        return priority == Priority.FREE_PERIOD;
    }

    @Override
    public SelectedTimetable generate(
            List<Curriculum> candidates,
            SelectedTimetable timeTable,
            RecommendRequest.createRecommendRequest request
    ) {
        List<Curriculum> remaining = new ArrayList<>(candidates);
        Collections.shuffle(remaining); // 동점 후보들의 순서를 랜덤화 (comparator에서 tie-break)

        long deadline = System.nanoTime()+ recommendExpired;

        // 백트래킹으로 "정확히 targetCredits"를 맞추는 조합을 탐색
        backtrack(remaining, timeTable, request,deadline);

        return timeTable;
    }

    private boolean backtrack(
            List<Curriculum> remaining,
            SelectedTimetable timeTable,
            RecommendRequest.createRecommendRequest request,
            long deadline
    ) {
        // 알고리즘 시간 초과 케이스
        if (System.nanoTime() > deadline) {
            throw new CustomException(RecommendErrorCode.RECOMMEND_TIME_EXCEED);
        }

        int target = request.targetCredits();
        int current = timeTable.getTotalCredit();

        if (current == target) {
            return true;
        }

        int diff = target - current;

        // 남은 강의 학점으로 학점 달성이 불가능하면 가지치기
        if (!canReachExactCredit(remaining, diff)) {
            return false;
        }

        Set<Week> usedDays = timeTable.getUsedDays();

        List<Curriculum> sorted = remaining.stream()
                .filter(c -> current + c.getLecture().getCredit() <= target)
                .filter(c-> timeTable.canAdd(c.getLecture()))
                .sorted(dynamicFreeDayComparator(request.grade(), usedDays))
                .toList();

        for (Curriculum candidate : sorted) {
            Lecture lecture = candidate.getLecture();

            timeTable.add(lecture);
            remaining.remove(candidate);

            if (backtrack(remaining, timeTable, request,deadline)) {
                return true; // 성공했으면 그대로 리턴
            }

            // 되돌리고 다음 후보 시도
            timeTable.remove(lecture);
            remaining.add(candidate);
        }

        return false; // 모든 후보를 다 시도했는데도 실패
    }


    // remaining에 있는 강의들의 학점만으로(시간충돌 무시) diff를 정확히 만들 수 있는지 확인하는 DP.
    private boolean canReachExactCredit(List<Curriculum> remaining, int diff) {
        if (diff == 0) return true;
        if (diff < 0) return false;

        boolean[] dp = new boolean[diff + 1];
        dp[0] = true;

        for (Curriculum c : remaining) {
            int credit = c.getLecture().getCredit();
            if (credit <= 0 || credit > diff) continue;
            for (int s = diff; s >= credit; s--) {
                if (dp[s - credit]) {
                    dp[s] = true;
                }
            }
        }
        return dp[diff];
    }

    private Comparator<Curriculum> dynamicFreeDayComparator(int userGrade, Set<Week> usedDays) {
        return Comparator
                .comparing((Curriculum c) -> c.getGrade() > userGrade)
                .thenComparing(c -> !c.getIsRequired())
                .thenComparingLong(c -> newDaysCount(c.getLecture(), usedDays))
                .thenComparing(Curriculum::getGrade)
                .thenComparing(c -> c.getLecture().getCredit());
    }

    private long newDaysCount(Lecture lecture, Set<Week> usedDays) {
        return lecture.getLectureSchedules().stream()
                .map(LectureSchedule::getDayOfWeek)
                .distinct()
                .filter(day -> !usedDays.contains(day))
                .count();
    }
}
package org.example.zzazo.domain.recommend.strategy;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.exception.RecommendErrorCode;
import org.example.zzazo.global.error.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class LectureCriteriaStrategy implements RecommendStrategy {
    @Value("${recommend.timeout}")
    private long recommendExpired;

    @Override
    public boolean supports(Priority priority) {
        return priority == Priority.LECTURE_CRITERIA; // 기본 전략
    }

    @Override
    public SelectedTimetable generate(
            List<Curriculum> candidates,
            SelectedTimetable timeTable,
            RecommendRequest.createRecommendRequest request
    ) {
        Long deadline = System.nanoTime()+ recommendExpired;

        Collections.shuffle(candidates);

        List<Curriculum> sorted = candidates.stream()
                .sorted(priorityComparator(request.grade()))
                .toList();

        boolean exact = fillExact(sorted, timeTable, request.targetCredits(),deadline);
        if (!exact) {
            throw new CustomException(RecommendErrorCode.RECOMMEND_NOT_EXISTS);
        }
        return timeTable;
    }


    private static boolean fillExact(
            List<Curriculum> candidates,
            SelectedTimetable timeTable,
            int targetCredits,
            long deadline
    ) {
        return dfs(candidates, 0, timeTable, targetCredits,deadline);
    }

    private static boolean dfs(
            List<Curriculum> candidates,
            int index,
            SelectedTimetable timeTable,
            int targetCredits,
            long deadline
    ) {
        if(System.nanoTime()>deadline) {
            throw new CustomException(RecommendErrorCode.RECOMMEND_TIME_EXCEED);
        }

        int current = timeTable.getTotalCredit();
        if (current == targetCredits) {
            return true;
        }
        if (index >= candidates.size()) {
            return false;
        }

        // 가지치기: 남은 후보를 다 더해도 목표에 못 미치면 이 경로는 볼 필요 없음
        int remainingMax = 0;
        for (int i = index; i < candidates.size(); i++) {
            remainingMax += candidates.get(i).getLecture().getCredit();
        }
        if (current + remainingMax < targetCredits) {
            return false;
        }

        Curriculum candidate = candidates.get(index);

        // 1) 포함 시도 (초과하지 않는 선에서, 우선순위 순서 유지)
        if (current + candidate.getLecture().getCredit() <= targetCredits
                && timeTable.canAdd(candidate.getLecture())) {
            timeTable.add(candidate.getLecture());
            if (dfs(candidates, index + 1, timeTable, targetCredits,deadline)) {
                return true;
            }
            timeTable.remove(candidate.getLecture()); // 실패하면 되돌리기
        }

        // 2) 제외하고 다음 후보
        return dfs(candidates, index + 1, timeTable, targetCredits,deadline);
    }

    private Comparator<Curriculum> priorityComparator(int userGrade) {
        return Comparator
                // 대상 학년 이하 먼저
                .comparing((Curriculum c) -> c.getGrade() > userGrade)
                // 필수 먼저
                .thenComparing(c -> !c.getIsRequired())
                // 그 안에서 학년순
                .thenComparing(Curriculum::getGrade)
                // 마지막 학점
                .thenComparing(c -> c.getLecture().getCredit());
    }
}
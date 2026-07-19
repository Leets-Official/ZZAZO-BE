package org.example.zzazo.domain.recommend.strategy;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class LectureCriteriaStrategy implements RecommendStrategy {

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
        List<Curriculum> sorted = candidates.stream()
                .sorted(priorityComparator(request.grade()))
                .toList();

        for (Curriculum candidate : sorted) {
            if (timeTable.getTotalCredit() >= request.targetCredits()) {
                break;
            }
            if (timeTable.canAdd(candidate.getLecture())) {
                timeTable.add(candidate.getLecture());
            }
        }
        return timeTable;
    }

    private Comparator<Curriculum> priorityComparator(int userGrade) {
        return Comparator
                .comparing((Curriculum c) -> c.getGrade() > userGrade)
                .thenComparing(Curriculum::getGrade)
                .thenComparing(c -> !c.getIsRequired())
                .thenComparing(c -> c.getLecture().getCredit());
    }
}
package org.example.zzazo.domain.recommend.strategy;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.global.common.Week;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FreePeriodStrategy implements RecommendStrategy {

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

        while (timeTable.getTotalCredit() < request.targetCredits() && !remaining.isEmpty()) {
            Set<Week> usedDays = timeTable.getUsedDays();

            Optional<Curriculum> next = remaining.stream()
                    .filter(c -> timeTable.canAdd(c.getLecture()))
                    .min(dynamicFreeDayComparator(request.grade(), usedDays));

            if (next.isEmpty()) {
                break;
            }

            Curriculum chosen = next.get();
            timeTable.add(chosen.getLecture());
            remaining.remove(chosen);
        }
        return timeTable;
    }

    private Comparator<Curriculum> dynamicFreeDayComparator(int userGrade, Set<Week> usedDays) {
        return Comparator
                .comparing((Curriculum c) -> c.getGrade() > userGrade)
                .thenComparing(c -> !c.getIsRequired())
                .thenComparingLong(c -> newDaysCount(c.getLecture(), usedDays))
                .thenComparing(Curriculum::getGrade)
                .thenComparing(c -> c.getLecture().getCredit());
    }

    private long newDaysCount(org.example.zzazo.domain.lecture.entity.Lecture lecture, Set<Week> usedDays) {
        return lecture.getLectureSchedules().stream()
                .map(LectureSchedule::getDayOfWeek)
                .distinct()
                .filter(day -> !usedDays.contains(day))
                .count();
    }
}
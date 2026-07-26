package org.example.zzazo.domain.recommend.strategy;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.domain.SelectedTimetable;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;

import java.util.List;

public interface RecommendStrategy {


    boolean supports(Priority priority);


    SelectedTimetable generate(
            List<Curriculum> candidates,
            SelectedTimetable timeTable,
            RecommendRequest.createRecommendRequest request
    );
}
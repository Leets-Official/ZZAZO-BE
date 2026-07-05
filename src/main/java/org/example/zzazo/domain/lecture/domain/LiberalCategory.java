package org.example.zzazo.domain.lecture.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum LiberalCategory {
    COMMUNICATION("의사소통"),
    GACHON_VISION("가천비전"),
    AI_BASIC("AI기초");

    private final String value;


}

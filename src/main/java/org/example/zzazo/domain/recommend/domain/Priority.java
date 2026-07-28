package org.example.zzazo.domain.recommend.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Priority {
    LECTURE_CRITERIA("수강기준 우선"),FREE_PERIOD("공강 우선");

    private final String value;
}

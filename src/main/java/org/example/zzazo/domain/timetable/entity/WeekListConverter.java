package org.example.zzazo.domain.timetable.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.zzazo.global.common.Week;

import java.util.Arrays;
import java.util.List;

@Converter
public class WeekListConverter implements AttributeConverter<List<Week>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Week> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        return String.join(DELIMITER, attribute.stream()
                .map(Week::name)
                .toList());
    }

    @Override
    public List<Week> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return List.of();
        }

        return Arrays.stream(dbData.split(DELIMITER))
                .map(Week::valueOf)
                .toList();
    }
}

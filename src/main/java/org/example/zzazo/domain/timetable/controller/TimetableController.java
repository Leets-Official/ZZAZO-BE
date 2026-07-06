package org.example.zzazo.domain.timetable.controller;

import jakarta.validation.Valid;
import org.example.zzazo.domain.timetable.controller.docs.TimetableControllerDocs;
import org.example.zzazo.domain.timetable.dto.TimetableCreateRequest;
import org.example.zzazo.domain.timetable.dto.TimetableCreateResponse;
import org.example.zzazo.domain.timetable.dto.TimetableDetailResponse;
import org.example.zzazo.domain.timetable.dto.TimetableListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableController implements TimetableControllerDocs {

    @Override
    @PostMapping
    public ResponseEntity<TimetableCreateResponse> createTimetable(
            @Valid @RequestBody TimetableCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TimetableCreateResponse(1L, "시간표가 저장되었습니다."));
    }

    @Override
    @GetMapping
    public ResponseEntity<TimetableListResponse> getTimetables() {
        return ResponseEntity.ok(TimetableListResponse.example());
    }

    @Override
    @GetMapping("/{timetableId}")
    public ResponseEntity<TimetableDetailResponse> getTimetable(
            @PathVariable Long timetableId
    ) {
        return ResponseEntity.ok(TimetableDetailResponse.example(timetableId));
    }

    @Override
    @DeleteMapping("/{timetableId}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long timetableId
    ) {
        return ResponseEntity.noContent().build();
    }
}

package org.example.zzazo.domain.lecture.controller;

import org.example.zzazo.domain.lecture.controller.docs.LectureControllerDocs;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.dto.LectureResponse;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController implements LectureControllerDocs {

    @Override
    @GetMapping("/major")
    public ApiResponse<LectureResponse.LectureListResponse> getMajorList(
            @RequestParam Long departmentId,
            @RequestParam int semester
    ) {
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK);
    }

    @Override
    @GetMapping("/liberal")
    public ApiResponse<LectureResponse.LectureListResponse> getLiberalList(
            @RequestParam LiberalCategory liberalCategory,
            @RequestParam int semester
    ) {
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK);
    }
}

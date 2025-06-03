package com.springboot.recommend.controller;

import com.springboot.recommend.dto.SurveyRequestDTO;
import com.springboot.recommend.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<String> submitSurvey(@RequestBody SurveyRequestDTO dto) { //json에 담긴걸 dto로 바꿔줌.
        surveyService.analyzeAndSave(dto);
        return ResponseEntity.ok("피부 정보가 저장되었습니다.");
    }

    @PutMapping
    public ResponseEntity<String> updateSurvey(@RequestBody SurveyRequestDTO dto) {
        surveyService.analyzeAndSave(dto);
        return ResponseEntity.ok("피부 정보가 업데이트되었습니다.");
    }
}



package com.springboot.recommend.service;

import com.springboot.recommend.dto.SurveyRequestDTO;
import com.springboot.recommend.entity.UserSkinInfo;
import com.springboot.recommend.repository.SurveyRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Service
public class SurveyService {

    private final SurveyRepository repository;

    public SurveyService(SurveyRepository repository) {
        this.repository = repository;
    }

    public void analyzeAndSave(SurveyRequestDTO dto) {
        // 1~5번 응답으로 피부타입 결정
        UserSkinInfo info = repository.findByUserId(dto.getUserId())
                .orElse(new UserSkinInfo()); // 없으면 새로 생성

        String skinType = determineSkinType(dto.getSkinTypeAnswers());

        // DB 저장

        info.setUserId(dto.getUserId()); //dto.getUserId()
        info.setSkinType(skinType);
        info.setSkinConcerns(dto.getSkinConcerns());
        info.setSensitivityLevel(dto.getSensitivityLevel());

        repository.save(info);
    }

    private String determineSkinType(List<String> answers) {
        Map<String, Long> counts = answers.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // 가장 많이 나온 답변
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    switch (entry.getKey()) {
                        case "A": return "지성";
                        case "B": return "복합성";
                        case "C": return "건성";
                        default: return "복합성";
                    }
                })
                .orElse("알 수 없음");
    }

    public void updateSurvey(SurveyRequestDTO dto) {
        Optional<UserSkinInfo> optional = repository.findByUserId(dto.getUserId());

            UserSkinInfo info = optional.get();
            String skinType = determineSkinType(dto.getSkinTypeAnswers());

            info.setSkinType(skinType);
            info.setSkinConcerns(dto.getSkinConcerns());
            info.setSensitivityLevel(dto.getSensitivityLevel());

            repository.save(info);

    }



}

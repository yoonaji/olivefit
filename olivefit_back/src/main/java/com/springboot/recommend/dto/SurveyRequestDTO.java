package com.springboot.recommend.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class SurveyRequestDTO {
    private Long userId;

    private List<String> skinTypeAnswers;     // 1~5번 질문 (예: ["A", "A", "B", "A", "C"])
    private String skinConcerns;        // 6번 질문 (예: ["여드름", "모공"])
    private String sensitivityLevel;          // 7번 질문 (예: "민감함")

}

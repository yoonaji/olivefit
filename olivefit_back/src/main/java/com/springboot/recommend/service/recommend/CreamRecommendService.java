package com.springboot.recommend.service.recommend;

import com.springboot.recommend.dto.ProductResponseDTO;
import com.springboot.recommend.repository.CreamProductRepository;
import com.springboot.recommend.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreamRecommendService {

    private final CreamProductRepository creamProductRepository;
    private final SurveyRepository surveyRepository;

    public CreamRecommendService(CreamProductRepository repo, SurveyRepository surveyRepo) {
        this.creamProductRepository = repo;
        this.surveyRepository = surveyRepo;
    }

    public List<ProductResponseDTO> recommendForUser(Long userId) {
        String skinType = surveyRepository.findSkinTypeByUserId(userId);

        return creamProductRepository.findBySkinType(skinType).stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getBrand(),
                        p.getName(),
                        p.getLink(),
                        p.getPrice(),
                        p.getImage(),
                        p.getSkinType(),
                        p.getScore(),
                        p.getSkinConcern(),
                        p.getIrritationLevel()
                ))
                .collect(Collectors.toList());
    }
}

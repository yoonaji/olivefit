package com.springboot.recommend.service.recommend;

import com.springboot.recommend.dto.ProductResponseDTO;
import com.springboot.recommend.repository.SkinProductRepository;
import com.springboot.recommend.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkinRecommendService {

    private final SkinProductRepository skinProductRepository;
    private final SurveyRepository surveyRepository;

    public SkinRecommendService(SkinProductRepository repo, SurveyRepository surveyRepo) {
        this.skinProductRepository = repo;
        this.surveyRepository = surveyRepo;
    }

    public List<ProductResponseDTO> recommendForUser(Long userId) {
        String skinType = surveyRepository.findSkinTypeByUserId(userId);

        return skinProductRepository.findBySkinType(skinType).stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(), p.getBrand(), p.getName(), p.getLink(),
                        p.getPrice(), p.getImage(), p.getSkinType(),
                        p.getScore(), p.getSkinConcern(), p.getIrritationLevel()
                ))
                .collect(Collectors.toList());
    }
}

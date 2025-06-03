package com.springboot.recommend.service.recommend;

import com.springboot.recommend.dto.ProductResponseDTO;
import com.springboot.recommend.repository.AllInOneProductRepository;
import com.springboot.recommend.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllInOneRecommendService {

    private final AllInOneProductRepository allInOneProductRepository;
    private final SurveyRepository surveyRepository;

    public AllInOneRecommendService(AllInOneProductRepository repo, SurveyRepository surveyRepo) {
        this.allInOneProductRepository = repo;
        this.surveyRepository = surveyRepo;
    }

    public List<ProductResponseDTO> recommendForUser(Long userId) {
        String skinType = surveyRepository.findSkinTypeByUserId(userId);
        System.out.println("사용자 skinType: " + skinType);

        return allInOneProductRepository.findBySkinTypeContaining(skinType).stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(), p.getBrand(), p.getName(), p.getLink(),
                        p.getPrice(), p.getImage(), p.getSkinType(),
                        p.getScore(), p.getSkinConcern(), p.getIrritationLevel()
                ))
                .collect(Collectors.toList());
    }
}

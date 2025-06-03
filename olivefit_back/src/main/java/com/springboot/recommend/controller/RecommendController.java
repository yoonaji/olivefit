package com.springboot.recommend.controller;

import com.springboot.recommend.dto.ProductResponseDTO;
import com.springboot.recommend.service.recommend.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//컨트롤러의 역할을 api가 받아오면 그에 맞게 서비스에게 넘기는 것.
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {


    //다음과 같은 서비스가 필요하다고 선언
    private final CreamRecommendService creamService;
    private final AllInOneRecommendService allInOneService;
    private final LotionRecommendService lotionService;
    private final EssenceRecommendService essenceService;
    private final SkinRecommendService skinService;
    private final SetRecommendService setService;

    //컨트롤러에서 쓸 서비스를 알아서 넣어주렴.
    public RecommendController(
            CreamRecommendService creamService,
            AllInOneRecommendService allInOneService,
            LotionRecommendService lotionService,
            EssenceRecommendService essenceService,
            SkinRecommendService skinService,
            SetRecommendService setService
    ) {
        this.creamService = creamService;
        this.allInOneService = allInOneService;
        this.lotionService = lotionService;
        this.essenceService = essenceService;
        this.skinService = skinService;
        this.setService = setService;
    }

    @GetMapping("/cream/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendCream(@PathVariable Long userId) {
        return ResponseEntity.ok(creamService.recommendForUser(userId));
    }

    @GetMapping("/allinone/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendAllInOne(@PathVariable Long userId) {
        return ResponseEntity.ok(allInOneService.recommendForUser(userId));
    }

    @GetMapping("/lotion/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendLotion(@PathVariable Long userId) {
        return ResponseEntity.ok(lotionService.recommendForUser(userId));
    }

    @GetMapping("/essence/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendEssence(@PathVariable Long userId) {
        return ResponseEntity.ok(essenceService.recommendForUser(userId));
    }

    @GetMapping("/skin/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendSkin(@PathVariable Long userId) {
        return ResponseEntity.ok(skinService.recommendForUser(userId));
    }

    @GetMapping("/set/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> recommendSet(@PathVariable Long userId) {
        return ResponseEntity.ok(setService.recommendForUser(userId));
    }
}

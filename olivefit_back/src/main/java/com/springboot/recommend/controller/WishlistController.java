package com.springboot.recommend.controller;

import com.springboot.recommend.entity.Wishlist;
import com.springboot.recommend.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody Wishlist dto) {
        if (wishlistRepository.findByUserIdAndProductIdAndCategory(dto.getUserId(), dto.getProductId(), dto.getCategory()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 찜한 제품입니다.");
        }
        wishlistRepository.save(dto);
        return ResponseEntity.ok("찜 완료");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistRepository.findByUserId(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(@RequestBody Wishlist dto) {
        wishlistRepository.deleteByUserIdAndProductIdAndCategory(dto.getUserId(), dto.getProductId(), dto.getCategory());
        return ResponseEntity.ok("찜 삭제 완료");
    }
}

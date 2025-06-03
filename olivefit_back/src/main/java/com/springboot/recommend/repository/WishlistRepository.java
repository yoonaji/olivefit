package com.springboot.recommend.repository;

import com.springboot.recommend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndProductIdAndCategory(Long userId, Long productId, String category);
    void deleteByUserIdAndProductIdAndCategory(Long userId, Long productId, String category);
}

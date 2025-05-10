package com.springboot.recommend.repository;

import com.springboot.recommend.entity.SetProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetProductRepository extends JpaRepository<SetProduct, Long> {
    List<SetProduct> findBySkinTypeContaining(String skinType);
}

package com.springboot.recommend.repository;

import com.springboot.recommend.entity.UserSkinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyRepository extends JpaRepository<UserSkinInfo, Long> {
    @Query("SELECT s.skinType FROM UserSkinInfo s WHERE s.userId = :userId")
    String findSkinTypeByUserId(@Param("userId") Long userId);
}
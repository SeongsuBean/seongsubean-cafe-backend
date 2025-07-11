package com.oopsw.seongsubeancafebackend.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CafeRepository extends JpaRepository<CafeEntity, Long> {
  @Query("SELECT c FROM CafeEntity c " +
      "WHERE c.cafeName LIKE %:keyword% " +
      "OR c.cafeAddress LIKE %:keyword% " +
      "OR c.cafeDetailAddress LIKE %:keyword% " +
      "OR c.cafeIntroduction LIKE %:keyword%")
  List<CafeEntity> findByKeyword(@Param("keyword") String keyword);

  @Query(value = "SELECT * FROM cafe_info ORDER BY RAND() LIMIT :limit OFFSET :offset", nativeQuery = true)
  List<CafeEntity> findRandomCafes(@Param("limit") int limit, @Param("offset") int offset);

}

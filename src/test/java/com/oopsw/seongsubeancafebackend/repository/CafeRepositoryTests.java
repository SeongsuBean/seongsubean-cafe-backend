package com.oopsw.seongsubeancafebackend.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class CafeRepositoryTests {
  @Autowired
  CafeRepository cafeRepository;

  @Test
  @Order(1)
  public void saveApprovedCafeEntity_Success() {
    // given
    CafeEntity cafeEntity = CafeEntity.builder()
            .email("taylor1213@gmail.com")
            .cafeName("따우전드 성수")
            .businessLicense("/cafes/businessLicense/thousandLicensce.png")
            .zipCode("04798")
            .cafeAddress("서울특별시 성동구 연무장1길 7")
            .cafeDetailAddress("1층")
            .phoneNumber("010-1234-5678")
            .cafeIntroduction("미국식 파이를 전문으로 판매하는 카페")
            .image("/cafes/thousand.png")
            .operationTimeText("MON:09:00-18:00;FRI:09:00-18:00")
            .isBusinessDay(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    // when
    CafeEntity saved = cafeRepository.save(cafeEntity);

    // then
    assertThat(saved).isNotNull();
    assertThat(saved.getCafeId()).isNotNull();
    assertThat(saved.getCafeName()).isEqualTo("따우전드 성수");
    assertThat(saved.getOperationTimeText()).contains("FRI:09:00-18:00");
  }

  //승인실패테스트

  @Test
  @Order(3)
  public void createCafeAdmin_ValidData_Success() {
    // given
    CafeEntity cafeEntity = CafeEntity.builder()
            .email("admin@cafe.com")
            .cafeName("성수 어드민 카페")
            .businessLicense("/cafes/businessLicense/seongsuadmincafe.png")
            .zipCode("04798")
            .cafeAddress("서울 성동구 뚝섬로 1길")
            .cafeDetailAddress("2층")
            .phoneNumber("010-1234-5678")
            .cafeIntroduction("관리자 등록 테스트 카페")
            .image("/images/cafe/admin-cafe.png")
            .isBusinessDay(true)
            .build();

    // when
    CafeEntity saved = cafeRepository.save(cafeEntity);

    // then
    assertThat(saved).isNotNull();
    assertThat(saved.getCafeId()).isNotNull();
    assertThat(saved.getCafeName()).isEqualTo("성수 어드민 카페");
  }

  @Test
  @Order(4)
  public void createCafeAdmin_MissingField_DataIntegrityViolationException() {
    // given
    CafeEntity incomplete = CafeEntity.builder()
            .email("admin2@cafe.com")
            .zipCode("04798")
            .build();

    // when & then
    assertThatThrownBy(() -> cafeRepository.save(incomplete))
            .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @Order(5)
  public void findCafeById_ExistingId_Success() {
    // given
    CafeEntity saved = cafeRepository.save(CafeEntity.builder()
            .cafeName("카페")
            .businessLicense("/cafes/businessLicense/cafe.png")
            .email("taylor1213@cafe.com")
            .zipCode("04798")
            .cafeAddress("서울 성동구 성수일로")
            .cafeDetailAddress("102호")
            .phoneNumber("010-1234-5678")
            .cafeIntroduction("진정한 성수 커피를 맛볼 수 있는 곳")
            .image("/images/cafe.png")
            .isBusinessDay(true)
            .operationTimeText("MON:09:00-18:00")
            .build());

    // when
    CafeEntity found = cafeRepository.findById(saved.getCafeId())
            .orElseThrow(() -> new RuntimeException("찾을 수 없음"));

    // then
    assertThat(found.getCafeName()).isEqualTo("카페");
  }

  @Test
  @Order(6)
  public void findCafeById_NonExistingId_EntityNotFoundException() {
    // expect
    assertThatThrownBy(() -> cafeRepository.findById(999L)
            .orElseThrow(() -> new EntityNotFoundException("없음")))
            .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Order(7)
  public void findByKeyword_ExactMatch_Success() {
    // given
    CafeEntity entity = CafeEntity.builder()
        .cafeName("성수파이카페")
        .businessLicense("/cafes/businessLicense/seongsupiecafe.png")
        .cafeIntroduction("파이가 맛있는 성수동 카페")
        .cafeAddress("서울 성동구")
        .zipCode("04798")
        .image("/images/sample.jpg")
        .email("example@email.com")
        .phoneNumber("010-1234-5678")
        .cafeDetailAddress("101호")
        .isBusinessDay(true)
        .build();
    cafeRepository.save(entity);

    // when
    List<CafeEntity> result = cafeRepository.findByKeyword("성수파이카페");

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getCafeName()).contains("성수파이카페");
  }

  @Test
  public void findByKeyword_NoMatch_ReturnsEmptyList() {
    //when
    List<CafeEntity> result = cafeRepository.findByKeyword("없는카페명");
    //then
    assertThat(result).isEmpty();
  }
}

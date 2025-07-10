package com.oopsw.seongsubeancafebackend.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class CafeRepositoryTests {
  @Autowired
  CafeRepository cafeRepository;

  @Test
  @Order(1)
  public void createCafe_ValidData_Success() {
    //given
    CafeRegisterEntity registerEntity = CafeRegisterEntity.builder()
        .email("taylor1213@gmail.com")
        .nickName("테일러수입푸드")
        .cafeName("따우전드 성수")
        .businessLicense("/cafes/businessLicense/thousandLicensce.png")
        .zipCode("04798")
        .cafeAddress("서울특별시 성동구 연무장1길 7")
        .cafeDetailAddress("1층")
        .cafeIntroduction("미국식 파이를 전문으로 판매하는 카페")
        .image("/cafes/thousand.png")
        .operationTimeText("MON:09:00-18:00, FRI:09:00-18:00")
        .build();
    // when
    cafeRepository.save(registerEntity);

    // then
    assertThat(registerEntity).isNotNull();
    assertThat(registerEntity.getCafeId()).isNotNull();
    assertThat(registerEntity.getCafeName()).isEqualTo("따우전드 성수");
    assertThat(registerEntity.getOperationTimeText()).contains("MON:09:00-18:00");
  }

  @Test
  @Order(2)
  public void createCafe_NullValue_DataIntegrityViolationException() {
    //given
    CafeRegisterEntity registerEntity = CafeRegisterEntity.builder()
        .email("taylor1213@gmail.com")
        .nickName("테일러수입푸드")
        .cafeName("따우전드 성수")
        .zipCode("04798")
        .cafeAddress("서울특별시 성동구 연무장1길 7")
        .cafeDetailAddress("1층")
        .image("/cafes/thousand.png")
        .operationTimeText("MON:09:00-18:00, FRI:09:00-18:00")
        .build();

    //when & then
    assertThatThrownBy(() -> cafeRepository.save(registerEntity))
    .isInstanceOf(DataIntegrityViolationException.class);

  }
}

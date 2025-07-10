package com.oopsw.seongsubeancafebackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class CafeServiceTests {
  @Autowired
  public CafeService cafeService;

  @Test
  @Order(1)
  public void createCafe_ValidData_Success() {
    //given
    CafeDTO cafeDTO = CafeDTO.builder()
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

    //when
    Long resultCafeId = cafeService.createCafe(cafeDTO);

    //then
    assertThat(resultCafeId).isEqualTo(1L);
  }

  @Test
  @Order(2)
  public void createCafe_NullValue_DataIntegrityViolationException_PropertyValueException() {
    //given
    CafeDTO cafeDTO = CafeDTO.builder()
        .email("taylor1213@gmail.com")
        .nickName("테일러수입푸드")
        .cafeName("따우전드 성수")
        .businessLicense(null)//의도적으로 null세팅 실패 테스트 유도
        .zipCode("04798")
        .cafeAddress("서울특별시 성동구 연무장1길 7")
        .cafeDetailAddress("1층")
        .cafeIntroduction("미국식 파이를 전문으로 판매하는 카페")
        .image("/cafes/thousand.png")
        .operationTimeText("MON:09:00-18:00, FRI:09:00-18:00")
        .build();

    //when & then
    //DataIntegrityViolationException
    //PropertyValueException
//    assertThatThrownBy(() -> cafeService.createCafe(cafeDTO))
//        .isInstanceOf(PropertyValueException.class); // 또는 DataIntegrityViolationException
    Throwable thrown = catchThrowable(() -> cafeService.createCafe(cafeDTO));
    assertThat(thrown)
        .isInstanceOfAny(DataIntegrityViolationException.class, PropertyValueException.class);
  }


}

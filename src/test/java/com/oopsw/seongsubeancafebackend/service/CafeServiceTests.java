package com.oopsw.seongsubeancafebackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterRepository;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import com.oopsw.seongsubeancafebackend.vo.RequestEmail;
import com.oopsw.seongsubeancafebackend.vo.RequestOwnerEditCafe;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class CafeServiceTests {
  @Autowired
  public CafeService cafeService;

  @Autowired
  private CafeRegisterRepository cafeRegisterRepository;

  @Autowired
  private CafeRepository cafeRepository;

  @BeforeEach
  void insertCafeData() {
    List<CafeEntity> cafes = new ArrayList<>();

    for (int i = 1; i <= 8; i++) {
      cafes.add(CafeEntity.builder()
              .cafeName("카페" + i)
              .businessLicense("BL-" + i)
              .zipCode("04700")
              .cafeAddress("서울 성수동 " + i + "길")
              .cafeDetailAddress("1층")
              .phoneNumber("010-0000-000" + i)
              .cafeIntroduction("소개" + i)
              .image("cafe" + i + ".jpg")
              .isBusinessDay(true)
              .email("test" + i + "@example.com")
              .build());
    }
    cafeRepository.saveAll(cafes);
  }

  @Test
  //@Order(1)
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

  //@Test
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

  //@Test
  @Order(3)
  public void approveCafe_ValidData_Success() {
    // given - 회원이 카페 등록 신청
    CafeRegisterEntity registerEntity = CafeRegisterEntity.builder()
            .email("taylor1213@gmail.com")
            .nickName("테일러수입푸드")
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
            .registerDate(LocalDateTime.now())
            .build();
    cafeRegisterRepository.save(registerEntity);

    // when - 관리자가 승인
    Long approvedCafeId = cafeService.approveCafe(registerEntity.getCafeId());
    cafeRepository.flush();
    cafeRegisterRepository.flush();

    // then - 실제 CafeEntity로 등록됐는지 확인
    CafeEntity savedCafe = cafeRepository.findById(approvedCafeId).orElse(null);

    assertThat(savedCafe).isNotNull();
    assertThat(savedCafe.getCafeName()).isEqualTo("따우전드 성수");
    assertThat(savedCafe.getOperationTimeText()).contains("MON:09:00-18:00");

    // 신청 테이블에서는 삭제되었는지 확인
    assertThat(cafeRegisterRepository.findById(registerEntity.getCafeId())).isEmpty();
  }

  //승인실패테스트
  //order4

  //@Test
  @Order(5)
  public void createCafeAdmin_ValidDTO_Success() {
    // given
    CafeDTO cafeDTO = CafeDTO.builder()
            .email("admin@cafe.com")
            .cafeName("성수 어드민 카페")
            .businessLicense("/cafes/businessLicense/seongsuadmincafe.png")
            .zipCode("04798")
            .cafeAddress("서울 성동구 연무장길")
            .cafeDetailAddress("2층")
            .phoneNumber("010-5678-1234")
            .cafeIntroduction("관리자 등록용 서비스 테스트")
            .image("/images/cafe/admin2.png")
            .isBusinessDay(true)
            .build();

    // when
    Long cafeId = cafeService.createCafeAdmin(cafeDTO);

    // then
    assertThat(cafeId).isNotNull();
    assertThat(cafeRepository.findById(cafeId)).isPresent();
  }

  //@Test
  @Order(6)
  public void createCafeAdmin_InvalidDTO_DataIntegrityViolationException() {
    // given
    CafeDTO incomplete = CafeDTO.builder()
            .email("fail@admin.com")
            .zipCode("04798")
            .build();

    // when & then
    assertThatThrownBy(() -> cafeService.createCafeAdmin(incomplete))
            .isInstanceOf(DataIntegrityViolationException.class);
  }

  //@Test
  @Order(7)
  public void getCafeById_ValidId_ReturnsDTO() {
    // given
    CafeEntity saved = cafeRepository.save(CafeEntity.builder()
            .cafeName("바나프레소 성수점")
            .businessLicense("/cafes/businessLicense/banacafe.png")
            .email("banana@cafe.com")
            .zipCode("04799")
            .cafeAddress("서울 성동구 아차산로")
            .cafeDetailAddress("2층")
            .phoneNumber("010-5678-1234")
            .cafeIntroduction("달콤한 디저트와 함께하는 카페")
            .image("/images/banana.png")
            .isBusinessDay(true)
            .operationTimeText("TUE:10:00-19:00")
            .build());

    // when
    CafeDTO dto = cafeService.getCafeById(saved.getCafeId());

    // then
    assertThat(dto).isNotNull();
    assertThat(dto.getCafeName()).isEqualTo("바나프레소 성수점");
  }

  //@Test
  @Order(8)
  public void getCafeById_InvalidId_EntityNotFoundException() {
    assertThatThrownBy(() -> cafeService.getCafeById(12345L))
            .isInstanceOf(EntityNotFoundException.class);
  }

  //@Test
  @Order(9)
  @Transactional
  public void searchCafes_ValidKeyword_ReturnsCafeDTOList() {
    //given
    CafeEntity entity = CafeEntity.builder()
        .cafeName("성수브루잉")
        .businessLicense("/cafes/businessLicense/seongsubu.png")
        .cafeIntroduction("핸드드립과 수제 맥주를 파는 카페")
        .cafeAddress("서울 성동구")
        .zipCode("04799")
        .image("/images/sample2.jpg")
        .email("admin@brew.com")
        .phoneNumber("010-5678-1234")
        .cafeDetailAddress("지하 1층")
        .isBusinessDay(true)
        .build();
    cafeRepository.save(entity);
    cafeRepository.flush(); // 바로 DB 반영
    //when
    List<ResponseCafe> result = cafeService.searchCafes("브루잉");

    //then
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getCafeName()).contains("성수브루잉");
  }

  //@Test
  @Order(10)
  public void searchCafes_NoMatch_ReturnsEmptyList() {
    List<ResponseCafe> result = cafeService.searchCafes("없는검색어");
    assertThat(result).isEmpty();
  }

  //@Test
  @Order(11)
  public void getCafeCards_ValidData_Success() {
    // given
    int page = 0;
    int size = 4;

    // when
    List<ResponseCafe> randomCafes = cafeService.getCafeCards(page, size);

    // then
    assertThat(randomCafes).isNotNull();
    assertThat(randomCafes.size()).isLessThanOrEqualTo(4);
  }

  //카페조회 4카드뷰 실패테스트
  //order(12)

  //@Test
  @Order(13)
  public void getMyCafes_ValidEmail_Success() {
    // given
    CafeEntity cafe1 = CafeEntity.builder()
        .cafeName("카페1")
        .email("owner@test.com")
        .businessLicense("1111111111")
        .zipCode("12345")
        .cafeAddress("서울시 성동구")
        .cafeDetailAddress("1층")
        .phoneNumber("010-0000-0001")
        .cafeIntroduction("카페1 소개")
        .isBusinessDay(true)
        .build();

    CafeEntity cafe2 = CafeEntity.builder()
        .cafeName("카페2")
        .email("owner@test.com")
        .businessLicense("2222222222")
        .zipCode("12345")
        .cafeAddress("서울시 성동구")
        .cafeDetailAddress("2층")
        .phoneNumber("010-0000-0002")
        .cafeIntroduction("카페2 소개")
        .isBusinessDay(true)
        .build();

    cafeRepository.saveAll(List.of(cafe1, cafe2));
    cafeRepository.flush();

    // when
    RequestEmail requestEmail = new RequestEmail("owner@test.com");
    List<ResponseCafe> result = cafeService.getMyCafes(requestEmail);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getCafeName()).isEqualTo("카페1");
    assertThat(result.get(1).getCafeName()).isEqualTo("카페2");
  }

  //내카페조회 실패테스트
  //order(14)

  @Test
  @Order(15)
  public void updateCafe_ValidCafeData_Success() {
    // given
    CafeEntity existing = cafeRepository.findAll().get(0); //첫번째 샘플 데이터
    Long cafeId = existing.getCafeId();

    RequestOwnerEditCafe request = RequestOwnerEditCafe.builder()
            .cafeId(cafeId)
            .cafeName("업데이트된 카페")
            .businessLicense("UPDATED-License")
            .zipCode("04799")
            .cafeAddress("서울 수정된 주소")
            .phoneNumber("010-1234-5678")
            .isBusinessDay(false)
            .build();

    // when
    ResponseCafe updated = cafeService.updateCafe(request);
    cafeRepository.flush();

    // then
    assertThat(updated.getCafeName()).isEqualTo("업데이트된 카페");
    assertThat(updated.getZipCode()).isEqualTo("04799");
    assertThat(updated.getCafeAddress()).isEqualTo("서울 수정된 주소");
    assertThat(updated.getPhoneNumber()).isEqualTo("010-1234-5678");
    assertThat(updated.getIsBusinessDay()).isFalse();
  }

  //카페 수정 실패테스트
  //order(16)
}

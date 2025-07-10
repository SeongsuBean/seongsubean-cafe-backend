package com.oopsw.seongsubeancafebackend.jpa;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CAFE_REGISTER_INFO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeRegisterEntity {
  //CafeRegisterApplicantEntity
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cafeId;

  @Column(name = "EMAIL", nullable = false, length = 320)
  private String email;
  @Column(name = "NICK_NAME", nullable = false, length = 200)
  private String nickName;

  @Column(name = "CAFE_NAME", nullable = false, length = 320)
  private String cafeName;
  @Column(name = "BUSINESS_LICENSE", nullable = false)
  private String businessLicense;
  @Column(name = "REGISTER_DATE", nullable = false)
  private LocalDateTime registerDate;

  @Column(name = "ZIPCODE", nullable = false, length = 20)
  private String zipCode;
  @Column(name = "CAFE_ADDRESS", nullable = false, length = 255)
  private String cafeAddress;
  @Column(name = "CAFE_DETAIL_ADDRESS", nullable = false, length = 255)
  private String cafeDetailAddress;
  @Column(name = "PHONE_NUMBER", length = 20)
  private String phoneNumber;
  @Column(name = "CAFE_INTRODUCTION", nullable = false, columnDefinition = "TEXT")
  private String cafeIntroduction;
  @Column(name = "IMAGE", length = 200)
  private String image;
  @Column(name = "IS_BUSINESS_DAY")
  private Boolean isBusinessDay;

  //영업시간
  @Column(name = "OPERATION_TIME_TEXT", columnDefinition = "TEXT")
  private String operationTimeText;

  @PrePersist
  public void prePersist() {
    if (this.registerDate == null) {
      this.registerDate = LocalDateTime.now();
    }
  }
}

package com.oopsw.seongsubeancafebackend.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "CAFE_INFO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cafeId;

  @Column(name = "EMAIL", nullable = false, length = 320)
  private String email;

  @Column(name = "CAFE_NAME", nullable = false, length = 500)
  private String cafeName;
  @Column(name = "BUSINESS_LICENSE", nullable = false)
  private String businessLicense;
  @Column(name = "ZIPCODE", nullable = false, length = 20)
  private String zipCode;
  @Column(name = "CAFE_ADDRESS", nullable = false, length = 255)
  private String cafeAddress;
  @Column(name = "CAFE_DETAIL_ADDRESS", nullable = false, length = 255)
  private String cafeDetailAddress;
  @Column(name = "PHONE_NUMBER", length = 20)
  private String phoneNumber;
  @Column(name = "CAFE_INTRODUCTION", columnDefinition = "TEXT")
  private String cafeIntroduction;
  @Column(name = "IMAGE", length = 200)
  private String image;
  @Column(name = "IS_BUSINESS_DAY", nullable = false)
  private Boolean isBusinessDay = true;

  //영업시간
  @Column(name = "OPERATION_TIME_TEXT", columnDefinition = "TEXT")
  private String operationTimeText;


  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.isBusinessDay = true;
  }
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
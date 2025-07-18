package com.oopsw.seongsubeancafebackend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeDTO {
  private Long cafeId;
  private String email;
  private String nickName;
  private String cafeName;
  private String businessLicense;
  private LocalDateTime registerDate;
  private String zipCode;
  private String cafeAddress;
  private String cafeDetailAddress;
  private String phoneNumber;
  private String cafeIntroduction;
  private String image;
  private Boolean isBusinessDay;
  private String operationTimeText;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

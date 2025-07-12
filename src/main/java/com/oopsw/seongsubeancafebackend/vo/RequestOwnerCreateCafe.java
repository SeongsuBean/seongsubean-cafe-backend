package com.oopsw.seongsubeancafebackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOwnerCreateCafe {
  private String cafeName;
  private String businessLicense;
  private String zipCode;
  private String cafeAddress;
  private String cafeDetailAddress;
  private String phoneNumber;
  private String cafeIntroduction;
  private String image;
  private Boolean isBusinessDay;
  private String operationTimeText;

  private String email;
  private String nickName;
}

package com.oopsw.seongsubeancafebackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOwnerEditCafe {
    private Long cafeId;

    private String cafeName;
    private String businessLicense;
    private String zipCode;
    private String cafeAddress;
    private String cafeDetailAddress;
    private String phoneNumber;
    private String cafeIntroduction;
    private String image;
    private String operationTimeText;
    private Boolean isBusinessDay; //휴무설정
}

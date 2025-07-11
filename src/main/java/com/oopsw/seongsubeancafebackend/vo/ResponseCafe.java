package com.oopsw.seongsubeancafebackend.vo;

import lombok.Data;

@Data
public class ResponseCafe {
    private Long cafeId;
    private String cafeName;
    private String cafeAddress;
    private String cafeDetailAddress;
    private String phoneNumber;
    private String cafeIntroduction;
    private String image;
    private Boolean isBusinessDay;
    private String operationTimeText;
}

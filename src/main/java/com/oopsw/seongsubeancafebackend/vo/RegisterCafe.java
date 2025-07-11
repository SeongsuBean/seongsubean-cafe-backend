package com.oopsw.seongsubeancafebackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCafe {
    private String cafeName;
    private String businessLicense;
    private String zipCode;
    private String cafeAddress;
    private String cafeDetailAddress;
    private Boolean isBusinessDay;
    private String operationTimeText;

    private String nickName;
    private String email;
}

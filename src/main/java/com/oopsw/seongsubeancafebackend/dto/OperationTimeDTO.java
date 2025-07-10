package com.oopsw.seongsubeancafebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationTimeDTO {
  private String weekday;
  private String openTime;
  private String closeTime;
}


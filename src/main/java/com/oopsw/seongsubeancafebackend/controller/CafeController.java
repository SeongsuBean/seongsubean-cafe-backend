package com.oopsw.seongsubeancafebackend.controller;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.service.CafeService;
import com.oopsw.seongsubeancafebackend.vo.RequestCafe;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {
  private final CafeService cafeService;

  //Vaild
  @PostMapping()
  public ResponseEntity<Map<String, Long>> addCafe(@RequestBody RequestCafe requestCafe) {
    CafeDTO cafeDTO = new ModelMapper().map(requestCafe, CafeDTO.class);
    Long resultCafeId = cafeService.createCafe(cafeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("cafeId", resultCafeId));
  }
}

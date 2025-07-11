package com.oopsw.seongsubeancafebackend.controller;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.service.CafeService;
import com.oopsw.seongsubeancafebackend.vo.RegisterCafe;
import com.oopsw.seongsubeancafebackend.vo.RequestCafe;

import java.util.List;
import java.util.Map;

import com.oopsw.seongsubeancafebackend.vo.RequestKeyword;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.RenderingResponse;

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

  @PostMapping("/{cafeId}")
  public ResponseEntity<Map<String, Long>> approveCafe(@PathVariable("cafeId") Long registerCafeId) {
    Long approvedCafeId = cafeService.approveCafe(registerCafeId);
    return ResponseEntity.ok(Map.of("approvedCafeId", approvedCafeId));
  }

}

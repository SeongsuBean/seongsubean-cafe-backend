package com.oopsw.seongsubeancafebackend.controller;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.service.CafeService;
import com.oopsw.seongsubeancafebackend.vo.RequestAdminCreateCafe;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerRestController {

  private final CafeService cafeService;
  //CafeController에서 관리자 권한 넘기기
  @PostMapping("/{cafeId}")
  public ResponseEntity<Map<String, Long>> approveCafe(@PathVariable("cafeId") Long registerCafeId) {
    Long approvedCafeId = cafeService.approveCafe(registerCafeId);
    return ResponseEntity.ok(Map.of("approvedCafeId", approvedCafeId));
  }
  @PostMapping()
  public ResponseEntity<Map<String, Long>> createCafeAdmin(@RequestBody RequestAdminCreateCafe requestAdminCreateCafe) {
    CafeDTO cafeDTO = new ModelMapper().map(requestAdminCreateCafe, CafeDTO.class);
    Long resultCafeId = cafeService.createCafeAdmin(cafeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("cafeId", resultCafeId));
  }


  //회원 카페 승인 대기 목록 불러오기
  @GetMapping("/cafes")
  public ResponseEntity<List<ResponseCafe>> getAllCafes() {
    List<ResponseCafe> cafes = cafeService.getAllCafes();
    return ResponseEntity.ok(cafes);
  }


}

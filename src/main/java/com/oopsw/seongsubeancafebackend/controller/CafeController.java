package com.oopsw.seongsubeancafebackend.controller;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.service.CafeService;
import com.oopsw.seongsubeancafebackend.vo.RegisterCafe;
import com.oopsw.seongsubeancafebackend.vo.RequestCafe;

import com.oopsw.seongsubeancafebackend.vo.RequestEmail;
import java.util.List;
import java.util.Map;

import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {
  private final CafeService cafeService;

  //Vaild
  @PostMapping()
  public ResponseEntity<Map<String, Long>> createCafe(@RequestBody RequestCafe requestCafe) {
    CafeDTO cafeDTO = new ModelMapper().map(requestCafe, CafeDTO.class);
    Long resultCafeId = cafeService.createCafe(cafeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("cafeId", resultCafeId));
  }

  @PostMapping("/{cafeId}")
  public ResponseEntity<Map<String, Long>> approveCafe(@PathVariable("cafeId") Long registerCafeId) {
    Long approvedCafeId = cafeService.approveCafe(registerCafeId);
    return ResponseEntity.ok(Map.of("approvedCafeId", approvedCafeId));
  }

  @PostMapping("/admin")
  public ResponseEntity<Map<String, Long>> createCafeAdmin(@RequestBody RegisterCafe registerCafe) {
    CafeDTO cafeDTO = new ModelMapper().map(registerCafe, CafeDTO.class);
    Long resultCafeId = cafeService.createCafeAdmin(cafeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("cafeId", resultCafeId));
  }

  @GetMapping("/{cafeId}")
  public ResponseEntity<ResponseCafe> getCafe(@PathVariable("cafeId") Long registerCafeId) {
    CafeDTO cafeDTO = cafeService.getCafeById(registerCafeId);
    ResponseCafe result = new ModelMapper().map(cafeDTO, ResponseCafe.class);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ResponseCafe>> searchCafes(@PathVariable ("keyword") String keyword) {
    List<ResponseCafe> cafeList = cafeService.searchCafes(keyword);
    return ResponseEntity.ok(cafeList);
  }

  @GetMapping("/random")
  public ResponseEntity<List<ResponseCafe>> getCafeCards(@RequestParam(defaultValue = "0") int page) {
    int size = 4;
    List<ResponseCafe> cafeList = cafeService.getCafeCards(page, size);
    return ResponseEntity.ok(cafeList);
  }

  @GetMapping("/mycafe")
  public ResponseEntity<List<ResponseCafe>> getMyCafes(@RequestBody RequestEmail email) {
    List<ResponseCafe> cafeList = cafeService.getMyCafes(email);
    return ResponseEntity.ok(cafeList);
  }

  @PutMapping("{cafeId}")
  public ResponseEntity<ResponseCafe> updateCafe(@RequestBody RequestCafe requestCafe,
      @PathVariable("cafeId") Long registerCafeId) {
    CafeDTO cafeDTO = new ModelMapper().map(requestCafe, CafeDTO.class);
    cafeDTO.setCafeId(registerCafeId);
    ResponseCafe response = new ModelMapper().map(cafeService.updateCafe(cafeDTO), ResponseCafe.class);
    return ResponseEntity.ok(response);
  }
}

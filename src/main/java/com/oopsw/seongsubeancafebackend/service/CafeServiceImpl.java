package com.oopsw.seongsubeancafebackend.service;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterRepository;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

  private final CafeRepository cafeRepository;
  private final CafeRegisterRepository cafeRegisterRepository;

  @Override
  public Long createCafe(CafeDTO cafeDTO) {
    CafeRegisterEntity cafeEntity = new ModelMapper().map(cafeDTO, CafeRegisterEntity.class);
    //cafeEntity를 저장
    try {
      CafeRegisterEntity resultCafeEntity = cafeRegisterRepository.save(cafeEntity);
      //cafeEntity의 cafeId를 반환
      return resultCafeEntity.getCafeId();
    } catch (Exception e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }
    //return cafeEntity.getCafeId();
  }

  @Transactional
  public Long approveCafe(Long registerCafeId) {
    // 1. 신청된 카페 정보 조회
    CafeRegisterEntity registerEntity = cafeRegisterRepository.findById(registerCafeId)
            .orElseThrow(() -> new EntityNotFoundException("등록 요청이 없습니다."));
    // 2. 등록된 엔티티를 실제 CafeEntity로 변환
    CafeEntity cafeEntity = new ModelMapper().map(registerEntity, CafeEntity.class);
    cafeEntity.setCafeId(null); //JPA가 새로 auto-increment하도록 유도

    // 3. 저장
    cafeRepository.save(cafeEntity);
    // 4. 신청 정보 삭제
    cafeRegisterRepository.deleteById(registerCafeId);
    // 5. 승인된 카페 ID 반환
    return cafeEntity.getCafeId();
  }

  @Override
  public Long createCafeAdmin(CafeDTO cafeDTO) {
    CafeEntity cafeEntity = new ModelMapper().map(cafeDTO, CafeEntity.class);
    //cafeEntity를 저장
    try {
      CafeEntity resultCafeEntity = cafeRepository.save(cafeEntity);
      //cafeEntity의 cafeId를 반환
      return resultCafeEntity.getCafeId();
    } catch (Exception e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }
  }



}

package com.oopsw.seongsubeancafebackend.service;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

  private final CafeRepository cafeRepository;

  @Override
  public Long createCafe(CafeDTO cafeDTO) {
    CafeRegisterEntity cafeEntity = new ModelMapper().map(cafeDTO, CafeRegisterEntity.class);
    //cafeEntity를 저장
    try {
      CafeRegisterEntity resultCafeEntity = cafeRepository.save(cafeEntity);

      //cafeEntity의 cafeId를 반환
      return resultCafeEntity.getCafeId();
    } catch (Exception e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }
    //return cafeEntity.getCafeId();
  }
}

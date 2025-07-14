package com.oopsw.seongsubeancafebackend.service;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.jpa.CafeEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterEntity;
import com.oopsw.seongsubeancafebackend.jpa.CafeRegisterRepository;
import com.oopsw.seongsubeancafebackend.jpa.CafeRepository;
import com.oopsw.seongsubeancafebackend.vo.RequestEmail;
import com.oopsw.seongsubeancafebackend.vo.RequestOwnerEditCafe;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
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

  @Override
  public CafeDTO getCafeById(Long cafeId) {
    CafeEntity entity = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new EntityNotFoundException("카페를 찾을 수 없습니다. ID: " + cafeId));
    return new ModelMapper().map(entity, CafeDTO.class);
  }

  @Override
  public List<ResponseCafe> searchCafes(String keyword) {
    List<CafeEntity> cafeList = cafeRepository.findByKeyword(keyword);
    ModelMapper mapper = new ModelMapper();
    return cafeList.stream()
        .map(cafe -> mapper.map(cafe, ResponseCafe.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<ResponseCafe> getCafeCards(int page, int size) {
    //int size =4;
    int offset = page * size;
    List<CafeEntity> cafeList = cafeRepository.findRandomCafes(size, offset);
    ModelMapper mapper = new ModelMapper();
    return cafeList.stream()
        .map(entity -> mapper.map(entity, ResponseCafe.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<ResponseCafe> getMyCafes(RequestEmail email) {
    List<CafeEntity> cafeEntities = cafeRepository.findByEmail(email.getEmail());
    return cafeEntities.stream()
        .map(entity -> new ModelMapper().map(entity, ResponseCafe.class))
        .collect(Collectors.toList());
  }

  @Transactional
  public ResponseCafe updateCafe(RequestOwnerEditCafe requestCafe) {
    // 1. DTO로 변환
    CafeDTO dto = new ModelMapper().map(requestCafe, CafeDTO.class);

    // 2. 기존 엔티티 조회
    CafeEntity entity = cafeRepository.findById(requestCafe.getCafeId())
            .orElseThrow(() -> new EntityNotFoundException("카페를 찾을 수 없습니다."));

    // 3. 직접 엔티티 수정 => 내부 메서드로 리팩토링 가능
    if (requestCafe.getCafeName() != null && !requestCafe.getCafeName().isBlank()) {
      entity.setCafeName(requestCafe.getCafeName());
    }
    if (requestCafe.getBusinessLicense() != null && !requestCafe.getBusinessLicense().isBlank()) {
      entity.setBusinessLicense(requestCafe.getBusinessLicense());
    }
    if (requestCafe.getZipCode() != null && !requestCafe.getZipCode().isBlank()) {
      entity.setZipCode(requestCafe.getZipCode());
    }
    if (requestCafe.getCafeAddress() != null && !requestCafe.getCafeAddress().isBlank()) {
      entity.setCafeAddress(requestCafe.getCafeAddress());
    }
    if (requestCafe.getPhoneNumber() != null && !requestCafe.getPhoneNumber().isBlank()) {
      entity.setPhoneNumber(requestCafe.getPhoneNumber());
    }
    if (requestCafe.getIsBusinessDay() != null) {
      entity.setIsBusinessDay(requestCafe.getIsBusinessDay());
    }

    // 4. 저장
    CafeEntity saved = cafeRepository.save(entity);

    // 5. 응답 반환
    return new ModelMapper().map(saved, ResponseCafe.class);
  }

  @Override
  public void deleteById(Long registerCafeId) {
    CafeEntity entity = cafeRepository.findById(registerCafeId)
            .orElseThrow(() -> new EntityNotFoundException("해당 카페를 찾을 수 없습니다."));
    cafeRepository.delete(entity);
  }

  @Override
  @Transactional //수정,변경 필수
  public void updateBusinessDay(Long cafeId) {
    CafeEntity cafe = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new EntityNotFoundException("카페를 찾을 수 없습니다. ID: " + cafeId));
    cafe.setIsBusinessDay(!cafe.getIsBusinessDay()); //DB에 있는 값 기준으로 반전
  }

  @Override
  public List<ResponseCafe> getAllCafes() {
    List<CafeEntity> cafeEntities = cafeRepository.findAll();
    return cafeEntities.stream()
        .map(entity -> new ModelMapper().map(entity, ResponseCafe.class))
        .collect(Collectors.toList());
  }
}

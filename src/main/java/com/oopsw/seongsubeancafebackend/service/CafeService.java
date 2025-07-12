package com.oopsw.seongsubeancafebackend.service;

import com.oopsw.seongsubeancafebackend.dto.CafeDTO;
import com.oopsw.seongsubeancafebackend.vo.RequestEmail;
import com.oopsw.seongsubeancafebackend.vo.ResponseCafe;

import java.util.List;

public interface CafeService {
  Long createCafe(CafeDTO cafeDTO);
  Long approveCafe(Long registerCafeId);
  Long createCafeAdmin(CafeDTO cafeDTO);
  CafeDTO getCafeById(Long cafeId);
  List<ResponseCafe> searchCafes(String keyword);
  List<ResponseCafe> getCafeCards(int page, int size);
  List<ResponseCafe> getMyCafes(RequestEmail email);
  ResponseCafe updateCafe(CafeDTO cafeDTO);
}

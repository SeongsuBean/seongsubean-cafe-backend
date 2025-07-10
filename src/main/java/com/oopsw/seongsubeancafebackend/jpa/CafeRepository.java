package com.oopsw.seongsubeancafebackend.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<CafeRegisterEntity, Long> {

}

package com.cloudbridge.repository;

import com.cloudbridge.entity.CivilPetitionStep;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CivilPetitionStepRepository extends JpaRepository<CivilPetitionStep, Long> {
    List<CivilPetitionStep> findByCpInfoIdOrderByStepOrderAsc(String cpInfoId);
}

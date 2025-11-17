package com.cloudbridge.repository;

import com.cloudbridge.entity.InfoPetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoPetitionRepository extends JpaRepository<InfoPetition, Long> {
    List<InfoPetition> findByCpInfoIdOrderByIdAsc(String cpInfoId);
}

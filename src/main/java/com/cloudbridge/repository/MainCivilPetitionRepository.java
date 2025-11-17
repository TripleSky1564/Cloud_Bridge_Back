package com.cloudbridge.repository;

import com.cloudbridge.entity.MainCivilPetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainCivilPetitionRepository extends
        JpaRepository<MainCivilPetition, String> {
    List<MainCivilPetition> findByCpNameContainingIgnoreCase(String cpName);
}

package com.cloudbridge.repository;

import com.cloudbridge.entity.PetitionWayOffline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetitionWayOfflineRepository extends JpaRepository<PetitionWayOffline, Long> {
    List<PetitionWayOffline> findByCpInfoIdOrderByIdAsc(String cpInfoId);
}

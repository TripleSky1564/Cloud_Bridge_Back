package com.cloudbridge.repository;

import com.cloudbridge.entity.PetitionWayOnline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetitionWayOnlineRepository extends JpaRepository<PetitionWayOnline, Long> {
    List<PetitionWayOnline> findByCpInfoIdOrderByIdAsc(String cpInfoId);
}

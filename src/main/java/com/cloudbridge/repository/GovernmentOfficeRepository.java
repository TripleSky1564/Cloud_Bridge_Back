package com.cloudbridge.repository;

import com.cloudbridge.entity.GovernmentOffice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GovernmentOfficeRepository extends JpaRepository<GovernmentOffice, Long> {
    List<GovernmentOffice> findByRegionCodeOrderByNameAsc(String regionCode);

    @Query("SELECT go FROM GovernmentOffice go ORDER BY go.name ASC")
    List<GovernmentOffice> findAllOrderByName();
}

package com.cloudbridge.repository;

import com.cloudbridge.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ✅ [추가]
import org.springframework.stereotype.Repository;

import java.util.List; // ✅ [추가]

/**
 * INSTITUTION 테이블을 조회하기 위한 Spring Data JPA 리포지토리
 */
@Repository
public interface InstitutionRepository extends JpaRepository<Institution, String> {

    @Query(value = """
        SELECT
            INSTITUTION_ID,
            INST_NAME,
            ADDRESS,
            ADDRESS_CODE,
            LATITUDE,
            LONGITUDE
        FROM INSTITUTION
        """, nativeQuery = true)
    List<Institution> findAllNative();
}

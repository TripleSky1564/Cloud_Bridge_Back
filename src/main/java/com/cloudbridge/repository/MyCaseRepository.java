package com.cloudbridge.repository;

import com.cloudbridge.entity.MyCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCaseRepository extends JpaRepository<MyCase, String> {

    List<MyCase> findByMemberId(String memberId);

    Optional<MyCase> findByMemberIdAndCpInfoId(String memberId, String cpInfoId);

    boolean existsByMemberIdAndCpInfoId(String memberId, String cpInfoId);
}

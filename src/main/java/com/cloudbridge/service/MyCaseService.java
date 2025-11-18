package com.cloudbridge.service;

import com.cloudbridge.dto.MyCaseDto;
import com.cloudbridge.entity.MyCase;
import com.cloudbridge.repository.MyCaseRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyCaseService {

    private final MyCaseRepository myCaseRepository;

    public MyCaseService(MyCaseRepository myCaseRepository) {
        this.myCaseRepository = myCaseRepository;
    }

    public List<MyCaseDto.Response> getCases(String memberId) {
        return myCaseRepository.findByMemberId(memberId).stream()
            .map(MyCaseDto.Response::new)
            .collect(Collectors.toList());
    }

    public MyCaseDto.Response upsertCase(MyCaseDto.Request request, String memberId) {
        MyCase entity = myCaseRepository.findByMemberIdAndCpInfoId(memberId, request.getCpInfoId())
            .orElseGet(() -> {
                MyCase mc = new MyCase();
                mc.setCaseId(UUID.randomUUID().toString());
                mc.setMemberId(memberId);
                mc.setCpInfoId(request.getCpInfoId());
                mc.setStartedAt(LocalDateTime.now());
                return mc;
            });

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
            if ("completed".equalsIgnoreCase(request.getStatus())) {
                entity.setCompletedAt(LocalDateTime.now());
            }
        }
        if (request.getChecklist() != null) {
            entity.setChecklist(request.getChecklist());
        }

        return new MyCaseDto.Response(myCaseRepository.save(entity));
    }

    public void deleteCase(String memberId, String cpInfoId) {
        myCaseRepository.findByMemberIdAndCpInfoId(memberId, cpInfoId)
            .ifPresent(myCaseRepository::delete);
    }
}

package com.cloudbridge.service;

import com.cloudbridge.dto.MainCivilPetitionDto;
import com.cloudbridge.dto.MainCivilPetitionDto.InstitutionSummary;
import com.cloudbridge.dto.MainCivilPetitionDto.PetitionStepDto;
import com.cloudbridge.dto.MainCivilPetitionDto.StepMode;
import com.cloudbridge.entity.CivilPetitionStep;
import com.cloudbridge.entity.InfoPetition;
import com.cloudbridge.entity.Institution;
import com.cloudbridge.entity.MainCivilPetition;
import com.cloudbridge.entity.PetitionWayOffline;
import com.cloudbridge.entity.PetitionWayOnline;
import com.cloudbridge.repository.CivilPetitionStepRepository;
import com.cloudbridge.repository.InfoPetitionRepository;
import com.cloudbridge.repository.MainCivilPetitionRepository;
import com.cloudbridge.repository.PetitionWayOfflineRepository;
import com.cloudbridge.repository.PetitionWayOnlineRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)

public class MainCivilPetitionService {

    private final MainCivilPetitionRepository repository;
    private final InfoPetitionRepository infoPetitionRepository;
    private final PetitionWayOfflineRepository petitionWayOfflineRepository;
    private final PetitionWayOnlineRepository petitionWayOnlineRepository;
    private final CivilPetitionStepRepository civilPetitionStepRepository;

    public MainCivilPetitionService(
            MainCivilPetitionRepository repository,
            InfoPetitionRepository infoPetitionRepository,
            PetitionWayOfflineRepository petitionWayOfflineRepository,
            PetitionWayOnlineRepository petitionWayOnlineRepository,
            CivilPetitionStepRepository civilPetitionStepRepository
    ) {
        this.repository = repository;
        this.infoPetitionRepository = infoPetitionRepository;
        this.petitionWayOfflineRepository = petitionWayOfflineRepository;
        this.petitionWayOnlineRepository = petitionWayOnlineRepository;
        this.civilPetitionStepRepository = civilPetitionStepRepository;
    }

    public List<MainCivilPetitionDto.Response> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public MainCivilPetitionDto.Response findById(String id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("CivilPetition not found: " + id));
    }

    public List<MainCivilPetitionDto.Response> searchByName(String q) {
        return repository.findByCpNameContainingIgnoreCase(q).stream()
                .map(this::toResponse)
                .toList();
    }

    private MainCivilPetitionDto.Response toResponse(MainCivilPetition petition) {
        List<String> descriptions = infoPetitionRepository.findByCpInfoIdOrderByIdAsc(petition.getInfoId())
                .stream()
                .map(InfoPetition::getDescription)
                .toList();

        List<CivilPetitionStep> orderedSteps = civilPetitionStepRepository
                .findByCpInfoIdOrderByStepOrderAsc(petition.getInfoId());

        List<PetitionStepDto> offlineSteps;
        List<PetitionStepDto> onlineSteps;

        if (!orderedSteps.isEmpty()) {
            offlineSteps = orderedSteps.stream()
                    .filter(step -> isOffline(step.getStepMode()))
                    .map(this::mapToDto)
                    .toList();

            onlineSteps = orderedSteps.stream()
                    .filter(step -> isOnline(step.getStepMode()))
                    .map(this::mapToDto)
                    .toList();
        } else {
            offlineSteps = legacyOfflineSteps(petition.getInfoId());
            onlineSteps = legacyOnlineSteps(petition.getInfoId());
        }

        return new MainCivilPetitionDto.Response(petition, descriptions, offlineSteps, onlineSteps);
    }

    private PetitionStepDto mapToDto(CivilPetitionStep step) {
        return new PetitionStepDto(
                step.getStepId(),
                step.getStepOrder(),
                toMode(step.getStepMode()),
                step.getContent(),
                step.getLinkUrl(),
                step.getInstitutions().stream()
                        .map(this::mapInstitution)
                        .toList()
        );
    }

    private List<PetitionStepDto> legacyOfflineSteps(String cpInfoId) {
        AtomicInteger order = new AtomicInteger(1);
        return petitionWayOfflineRepository.findByCpInfoIdOrderByIdAsc(cpInfoId)
                .stream()
                .map(PetitionWayOffline::getStep)
                .map(content -> new PetitionStepDto(
                        null,
                        order.getAndIncrement(),
                        StepMode.OFFLINE,
                        content,
                        null,
                        List.of()
                ))
                .toList();
    }

    private List<PetitionStepDto> legacyOnlineSteps(String cpInfoId) {
        AtomicInteger order = new AtomicInteger(1);
        return petitionWayOnlineRepository.findByCpInfoIdOrderByIdAsc(cpInfoId)
                .stream()
                .map(PetitionWayOnline::getStep)
                .map(content -> new PetitionStepDto(
                        null,
                        order.getAndIncrement(),
                        StepMode.ONLINE,
                        content,
                        null,
                        List.of()
                ))
                .toList();
    }

    private InstitutionSummary mapInstitution(Institution institution) {
        return new InstitutionSummary(
                institution.getInstitutionId(),
                institution.getInstName(),
                institution.getAddress(),
                institution.getPhone(),
                institution.getLatitude(),
                institution.getLongitude()
        );
    }

    private StepMode toMode(Integer stepMode) {
        if (stepMode == null) {
            return StepMode.ONLINE;
        }
        return switch (stepMode) {
            case 0 -> StepMode.OFFLINE;
            case 2 -> StepMode.HYBRID;
            default -> StepMode.ONLINE;
        };
    }

    private boolean isOffline(Integer mode) {
        return mode != null && (mode == 0 || mode == 2);
    }

    private boolean isOnline(Integer mode) {
        return mode == null || mode == 1 || mode == 2;
    }
}

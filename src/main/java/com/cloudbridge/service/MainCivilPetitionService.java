package com.cloudbridge.service;

import com.cloudbridge.dto.MainCivilPetitionDto;
import com.cloudbridge.entity.InfoPetition;
import com.cloudbridge.entity.MainCivilPetition;
import com.cloudbridge.entity.PetitionWayOffline;
import com.cloudbridge.entity.PetitionWayOnline;
import com.cloudbridge.repository.InfoPetitionRepository;
import com.cloudbridge.repository.MainCivilPetitionRepository;
import com.cloudbridge.repository.PetitionWayOfflineRepository;
import com.cloudbridge.repository.PetitionWayOnlineRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)

public class MainCivilPetitionService {

    private final MainCivilPetitionRepository repository;
    private final InfoPetitionRepository infoPetitionRepository;
    private final PetitionWayOfflineRepository petitionWayOfflineRepository;
    private final PetitionWayOnlineRepository petitionWayOnlineRepository;

    public MainCivilPetitionService(
            MainCivilPetitionRepository repository,
            InfoPetitionRepository infoPetitionRepository,
            PetitionWayOfflineRepository petitionWayOfflineRepository,
            PetitionWayOnlineRepository petitionWayOnlineRepository
    ) {
        this.repository = repository;
        this.infoPetitionRepository = infoPetitionRepository;
        this.petitionWayOfflineRepository = petitionWayOfflineRepository;
        this.petitionWayOnlineRepository = petitionWayOnlineRepository;
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

        List<String> offlineSteps = petitionWayOfflineRepository.findByCpInfoIdOrderByIdAsc(petition.getInfoId())
                .stream()
                .map(PetitionWayOffline::getStep)
                .toList();

        List<String> onlineSteps = petitionWayOnlineRepository.findByCpInfoIdOrderByIdAsc(petition.getInfoId())
                .stream()
                .map(PetitionWayOnline::getStep)
                .toList();

        return new MainCivilPetitionDto.Response(petition, descriptions, offlineSteps, onlineSteps);
    }
}

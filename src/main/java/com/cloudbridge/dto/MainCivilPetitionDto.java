package com.cloudbridge.dto;

import com.cloudbridge.entity.MainCivilPetition;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MainCivilPetitionDto {

    /**
     * 1. 프론트엔드 -> 백엔드 요청용 DTO
     */
    @Getter
    @Setter
    public static class AuthRequest {
        private String infoId;
        private String cpName;
        private String description;
        private String simple;
        private String offline;
        private String online;
    }

    /**
     * 2. 백엔드 -> 프론트엔드 응답용 DTO
     */
    @Getter
    public static class Response {
        private String infoId;
        private String cpName;
        private String simple;
        private List<String> descriptions;
        private List<PetitionStepDto> offlineSteps;
        private List<PetitionStepDto> onlineSteps;

        public Response(
                MainCivilPetition mainCivilPetition,
                List<String> descriptions,
                List<PetitionStepDto> offlineSteps,
                List<PetitionStepDto> onlineSteps
        ) {
            this.infoId = mainCivilPetition.getInfoId();
            this.cpName = mainCivilPetition.getCpName();
            this.simple = mainCivilPetition.getSimple();
            this.descriptions = descriptions;
            this.offlineSteps = offlineSteps;
            this.onlineSteps = onlineSteps;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PetitionStepDto {
        private Long id;
        private Integer order;
        private StepMode mode;
        private String content;
        private String linkUrl;
        private List<InstitutionSummary> institutions;
    }

    @Getter
    @AllArgsConstructor
    public static class InstitutionSummary {
        private String id;
        private String name;
        private String address;
        private String phone;
        private String latitude;
        private String longitude;
    }

    public enum StepMode {
        ONLINE,
        OFFLINE,
        HYBRID
    }
}

package com.cloudbridge.dto;

import com.cloudbridge.entity.GovernmentOffice;
import com.cloudbridge.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 프론트엔드에 관공서 정보를 전달하기 위한 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeDto {
    private String id;
    private String name;
    private String regionCode;
    private String category;
    private String address;
    private String phone;
    private String openingHours;
    private String notes;
    private Double latitude;
    private Double longitude;

    public static OfficeDto fromGovernmentOffice(GovernmentOffice entity) {
        if (entity == null) {
            return null;
        }
        return OfficeDto.builder()
            .id(entity.getId() != null ? entity.getId().toString() : null)
            .name(entity.getName())
            .regionCode(entity.getRegionCode())
            .category(entity.getCategory())
            .address(entity.getAddress())
            .phone(entity.getPhone())
            .openingHours(entity.getOpeningHours())
            .notes(entity.getNotes())
            .latitude(entity.getLatitude())
            .longitude(entity.getLongitude())
            .build();
    }

    public static OfficeDto fromInstitution(Institution entity, String regionCode, String category) {
        if (entity == null) {
            return null;
        }
        return OfficeDto.builder()
            .id(entity.getInstitutionId())
            .name(entity.getInstName())
            .regionCode(regionCode)
            .category(category)
            .address(entity.getAddress())
            .latitude(parseCoordinate(entity.getLatitude()))
            .longitude(parseCoordinate(entity.getLongitude()))
            .build();
    }

    private static Double parseCoordinate(String rawValue) {
        if (!StringUtils.hasText(rawValue)) {
            return null;
        }
        try {
            return Double.parseDouble(rawValue);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}

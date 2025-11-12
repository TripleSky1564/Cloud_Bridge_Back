package com.cloudbridge.service;

import com.cloudbridge.dto.OfficeDto;
import com.cloudbridge.entity.GovernmentOffice;
import com.cloudbridge.entity.Institution;
import com.cloudbridge.repository.GovernmentOfficeRepository;
import com.cloudbridge.repository.InstitutionRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class GovernmentOfficeService {

    private static final Logger log = LoggerFactory.getLogger(GovernmentOfficeService.class);

    private static final Map<Integer, String> REGION_CODE_MAP = Map.of(
        1, "gwangju-dong",
        2, "gwangju-buk",
        3, "gwangju-nam",
        4, "gwangju-seo",
        5, "gwangju-gwangsan"
    );

    private static final Map<String, String> REGION_KEYWORDS = Map.of(
        "동구", "gwangju-dong",
        "서구", "gwangju-seo",
        "남구", "gwangju-nam",
        "북구", "gwangju-buk",
        "광산구", "gwangju-gwangsan"
    );

    private final GovernmentOfficeRepository governmentOfficeRepository;
    private final InstitutionRepository institutionRepository;

    public GovernmentOfficeService(GovernmentOfficeRepository governmentOfficeRepository,
        InstitutionRepository institutionRepository) {
        this.governmentOfficeRepository = governmentOfficeRepository;
        this.institutionRepository = institutionRepository;
    }

    public List<OfficeDto> getOffices(String regionCode) {
        return filterByRegion(fetchPreferredOffices(), regionCode);
    }

    public List<OfficeDto> findNearby(double latitude, double longitude, double radiusKm) {
        final double safeRadius = radiusKm <= 0 ? 2.0 : radiusKm;
        return fetchPreferredOffices().stream()
            .filter(office -> office.getLatitude() != null && office.getLongitude() != null)
            .map(office -> new DistanceProjection(office, distanceInKm(latitude, longitude,
                office.getLatitude(), office.getLongitude())))
            .filter(dp -> dp.distanceKm <= safeRadius)
            .sorted(Comparator.comparingDouble(dp -> dp.distanceKm))
            .map(dp -> {
                dp.office.setNotes(
                    String.format(Locale.KOREA, "현재 위치로부터 약 %.1fkm", dp.distanceKm));
                return dp.office;
            })
            .collect(Collectors.toList());
    }

    private List<OfficeDto> fetchPreferredOffices() {
        List<OfficeDto> institutionOffices = institutionRepository.findAllNative().stream()
            .map(this::mapInstitutionToOffice)
            .filter(dto -> dto != null && dto.getLatitude() != null && dto.getLongitude() != null)
            .sorted(Comparator.comparing(
                office -> StringUtils.hasText(office.getName()) ? office.getName() : "",
                String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());

        if (!institutionOffices.isEmpty()) {
            return institutionOffices;
        }

        return governmentOfficeRepository.findAllOrderByName().stream()
            .map(OfficeDto::fromGovernmentOffice)
            .collect(Collectors.toList());
    }

    private List<OfficeDto> filterByRegion(List<OfficeDto> offices, String regionCode) {
        if (!StringUtils.hasText(regionCode)) {
            return offices;
        }
        return offices.stream()
            .filter(office -> regionCode.equals(office.getRegionCode()))
            .collect(Collectors.toList());
    }

    private OfficeDto mapInstitutionToOffice(Institution institution) {
        if (institution == null) {
            return null;
        }

        String regionCode = resolveRegionCode(institution.getAddress(), institution.getAddressCode());
        String category = resolveCategory(institution.getInstName());
        OfficeDto dto = OfficeDto.fromInstitution(institution, regionCode, category);
        if (dto.getLatitude() == null || dto.getLongitude() == null) {
            log.debug("좌표 정보가 없어 제외된 기관: {}", institution.getInstName());
            return null;
        }
        return dto;
    }

    private String resolveRegionCode(String address, Integer addressCode) {
        if (addressCode != null && REGION_CODE_MAP.containsKey(addressCode)) {
            return REGION_CODE_MAP.get(addressCode);
        }
        if (!StringUtils.hasText(address)) {
            return null;
        }
        return REGION_KEYWORDS.entrySet().stream()
            .filter(entry -> address.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
    }

    private String resolveCategory(String name) {
        if (!StringUtils.hasText(name)) {
            return "civil";
        }
        String normalized = name.toLowerCase(Locale.KOREA);
        if (containsAny(normalized, "복지", "welfare", "보건", "사회")) {
            return "welfare";
        }
        if (containsAny(normalized, "일자리", "고용", "취업", "노동")) {
            return "employment";
        }
        return "civil";
    }

    private boolean containsAny(String source, String... keywords) {
        for (String keyword : keywords) {
            if (StringUtils.hasText(keyword) && source.contains(keyword.toLowerCase(Locale.KOREA))) {
                return true;
            }
        }
        return false;
    }

    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final double earthRadius = 6371.0; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private record DistanceProjection(OfficeDto office, double distanceKm) {
    }
}

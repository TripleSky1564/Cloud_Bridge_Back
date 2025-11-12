package com.cloudbridge.controller;

import com.cloudbridge.dto.MapConfigDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 지도 관련 공통 설정(예: 네이버 지도 키)을 제공하는 컨트롤러입니다.
 * 관공서 데이터는 {@link GovernmentOfficeController}가 담당합니다.
 */
@RestController
@RequestMapping("/api")
public class MapController {

    @Value("${naver.map.client-id}")
    private String naverMapClientId;

    @GetMapping("/map/config")
    public MapConfigDto getMapConfig() {
        return new MapConfigDto(naverMapClientId);
    }
}

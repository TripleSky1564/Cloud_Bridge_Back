package com.cloudbridge.controller;

import com.cloudbridge.dto.OfficeDto;
import com.cloudbridge.service.GovernmentOfficeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/offices")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5180", "http://127.0.0.1:5180"})
public class GovernmentOfficeController {

    private final GovernmentOfficeService governmentOfficeService;

    public GovernmentOfficeController(GovernmentOfficeService governmentOfficeService) {
        this.governmentOfficeService = governmentOfficeService;
    }

    @GetMapping
    public ResponseEntity<List<OfficeDto>> getOffices(
        @RequestParam(value = "regionId", required = false) String regionId) {
        return ResponseEntity.ok(governmentOfficeService.getOffices(regionId));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<OfficeDto>> getNearbyOffices(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam(name = "radiusKm", defaultValue = "2.0") double radiusKm) {
        return ResponseEntity.ok(governmentOfficeService.findNearby(lat, lng, radiusKm));
    }
}

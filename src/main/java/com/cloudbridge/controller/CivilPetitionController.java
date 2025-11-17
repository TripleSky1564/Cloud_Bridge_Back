package com.cloudbridge.controller;

import com.cloudbridge.dto.MainCivilPetitionDto;
import com.cloudbridge.service.MainCivilPetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/civil-petitions")
@CrossOrigin(origins = {"http://localhost:5173","http://127.0.0.1:5173", "http://localhost:5180", "http://127.0.0.1:5180"})

public class CivilPetitionController {

    private final MainCivilPetitionService service;

    public CivilPetitionController(MainCivilPetitionService service) {
        this.service = service;
    }

    //목록 + 검색 (q파라미터)
    @GetMapping
    public ResponseEntity<List<MainCivilPetitionDto.Response>> list(
            @RequestParam(value = "q", required = false) String q) {
        return ResponseEntity.ok((q == null || q.isBlank()) ? service.findAll() : service.searchByName(q));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<MainCivilPetitionDto.Response> one(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }
}

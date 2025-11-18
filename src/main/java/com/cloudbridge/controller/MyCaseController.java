package com.cloudbridge.controller;

import com.cloudbridge.dto.MyCaseDto;
import com.cloudbridge.service.MyCaseService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// helper to fetch member ID from the logged-in user, for demo assume phone? hmm. but we need actual provider.

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5180"}, allowCredentials = "true")
public class MyCaseController {

    private final MyCaseService myCaseService;

    public MyCaseController(MyCaseService myCaseService) {
        this.myCaseService = myCaseService;
    }

    @GetMapping
    public ResponseEntity<List<MyCaseDto.Response>> getCases(@RequestParam String memberId) {
        return ResponseEntity.ok(myCaseService.getCases(memberId));
    }

    @PostMapping
    public ResponseEntity<MyCaseDto.Response> upsertCase(
        @RequestParam String memberId,
        @RequestBody MyCaseDto.Request request
    ) {
        return ResponseEntity.ok(myCaseService.upsertCase(request, memberId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCase(
        @RequestParam String memberId,
        @RequestParam String cpInfoId
    ) {
        myCaseService.deleteCase(memberId, cpInfoId);
        return ResponseEntity.noContent().build();
    }
}

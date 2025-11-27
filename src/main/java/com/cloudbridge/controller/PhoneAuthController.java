package com.cloudbridge.controller;

import com.cloudbridge.service.PhoneVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PhoneVerificationService phoneVerificationService;

    public AuthController(PhoneVerificationService phoneVerificationService) {
        this.phoneVerificationService = phoneVerificationService;
    }

    // 📌 인증번호 발송
    @PostMapping("/phone/request")
    public ResponseEntity<?> requestPhoneVerification(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String phone = body.get("phone");

        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "휴대전화 번호가 없습니다."));
        }

        // 인증 코드 생성
        String code = phoneVerificationService.issueCode(phone);

        // 응답 메시지 생성
        String message = phoneVerificationService.createRequestMessage(code);

        return ResponseEntity.ok(Map.of(
                "message", message,
                "demoCode", code       // 프론트에서 시연용 코드 표시
        ));
    }

    // 📌 인증번호 검증
    @PostMapping("/phone/verify")
    public ResponseEntity<?> verifyPhoneCode(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code = body.get("code");

        if (phone == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "전화번호 또는 코드가 없습니다."));
        }

        boolean success = phoneVerificationService.verifyCode(phone, code);

        if (!success) {
            String failMsg = phoneVerificationService.createVerifyFailMessage();
            return ResponseEntity.status(400).body(Map.of("message", failMsg));
        }

        String successMsg = phoneVerificationService.createVerifySuccessMessage();
        return ResponseEntity.ok(Map.of("message", successMsg));
    }
}

package com.cloudbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PhoneVerificationDto {

    @Getter
    @Setter
    public static class Request {
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    public static class Verify {
        private String phone;
        private String code;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String message;
        private String demoCode;
    }
}

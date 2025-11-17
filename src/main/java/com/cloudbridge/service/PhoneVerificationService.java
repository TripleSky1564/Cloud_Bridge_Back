package com.cloudbridge.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PhoneVerificationService {

    private static final int CODE_LENGTH = 6;
    private static final long EXPIRE_MINUTES = 5;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, VerificationEntry> store = new ConcurrentHashMap<>();
    private final MessageSource messageSource;

    public PhoneVerificationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String issueCode(String phone) {
        String code = generateCode();
        store.put(phone, new VerificationEntry(code, LocalDateTime.now().plusMinutes(EXPIRE_MINUTES)));
        return code;
    }

    public boolean verifyCode(String phone, String code) {
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code)) {
            return false;
        }
        VerificationEntry entry = store.get(phone);
        if (entry == null || entry.expireAt().isBefore(LocalDateTime.now())) {
            store.remove(phone);
            return false;
        }
        boolean matched = entry.code().equals(code);
        if (matched) {
            store.remove(phone);
        }
        return matched;
    }

    public String createRequestMessage(String code) {
        return messageSource.getMessage("phone.verification.request.success", new Object[]{code}, LocaleContextHolder.getLocale());
    }

    public String createVerifySuccessMessage() {
        return messageSource.getMessage("phone.verification.verify.success", null, LocaleContextHolder.getLocale());
    }

    public String createVerifyFailMessage() {
        return messageSource.getMessage("phone.verification.verify.fail", null, LocaleContextHolder.getLocale());
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private record VerificationEntry(String code, LocalDateTime expireAt) {}
}

package net.hovering.Hovering_Web.verification.controller;

import jakarta.mail.MessagingException;
import net.hovering.Hovering_Web.verification.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        try {
            verificationService.sendVerificationEmail(email);
            return ResponseEntity.ok("인증 코드가 이메일로 발송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("이메일 발송 실패");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = verificationService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 실패 또는 코드 만료");
        }
    }
}
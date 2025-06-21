package net.hovering.Hovering_Web.verification.service;

import jakarta.mail.MessagingException;
import net.hovering.Hovering_Web.verification.model.VerificationCode;
import net.hovering.Hovering_Web.verification.repository.VerificationCodeRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final JavaMailSender mailSender;

    public VerificationService(VerificationCodeRepository verificationCodeRepository, JavaMailSender mailSender) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailSender = mailSender;
    }

    public String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6자리 숫자 코드
    }

    public void sendVerificationEmail(String email) throws MessagingException {
        String code = generateCode();

        // 인증 코드 저장
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .build();
        verificationCodeRepository.save(verificationCode);

        // 이메일 발송
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("이메일 인증 코드");
        helper.setText("인증 코드: " + code, true);
        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmailAndCode(email, code);

        if (optionalCode.isPresent()) {
            VerificationCode verificationCode = optionalCode.get();
            if (!verificationCode.isVerified() && verificationCode.getExpiresAt().isAfter(LocalDateTime.now())) {
                verificationCode.setVerified(true);
                verificationCodeRepository.save(verificationCode);
                return true;
            }
        }
        return false;
    }
}
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

        // 기존 인증 코드 확인
        Optional<VerificationCode> existingCode = verificationCodeRepository.findByEmail(email);

        if (existingCode.isPresent()) {
            // 기존 코드 갱신
            VerificationCode verificationCode = existingCode.get();
            verificationCode.setCode(code);
            verificationCode.setCreatedAt(LocalDateTime.now());
            verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(3));
            verificationCode.setVerified(false);
            verificationCodeRepository.save(verificationCode);
        } else {
            // 새 코드 생성
            VerificationCode verificationCode = VerificationCode.builder()
                    .email(email)
                    .code(code)
                    .build();
            verificationCodeRepository.save(verificationCode);
        }

        // 이메일 발송
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);
        helper.setFrom("gyoseung0323@gmail.com");
        helper.setTo(email);
        helper.setSubject("[호버링] 이메일 인증 코드");
        helper.setText("인증 코드: " + code, true);
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String email) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);
        helper.setFrom("gyoseung0323@gmail.com");
        helper.setTo(email);
        helper.setSubject("회원가입을 축하합니다!");
        helper.setText("<h1>회원가입을 축하합니다!</h1><p>호버링에 가입해주셔서 감사합니다.</p>", true);
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
            } else if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                verificationCodeRepository.delete(verificationCode); // 만료된 코드 삭제
            }
        }
        return false;
    }
}
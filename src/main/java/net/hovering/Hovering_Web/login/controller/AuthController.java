package net.hovering.Hovering_Web.login.controller;

import jakarta.mail.MessagingException;
import net.hovering.Hovering_Web.join.model.Join;
import net.hovering.Hovering_Web.join.repository.JoinRepository;
import net.hovering.Hovering_Web.login.model.User;
import net.hovering.Hovering_Web.login.repository.UserRepository;
import net.hovering.Hovering_Web.verification.service.VerificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JoinRepository joinRepository;
    private final VerificationService verificationService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JoinRepository joinRepository, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.joinRepository = joinRepository;
        this.verificationService = verificationService;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // `login.html` 템플릿 반환
    }

    @GetMapping("/join")
    public String showJoinForm(Model model) {
        model.addAttribute("user", new User());
        return "join"; // 회원가입 페이지
    }

    @PostMapping("/join")
    public String processJoin(@ModelAttribute("user") User user,
                              @RequestParam("confirmPassword") String confirmPassword,
                              @RequestParam("verificationCode") String verificationCode,
                              Model model) {
        // 이메일 인증 확인
        boolean isVerified = verificationService.verifyCode(user.getEmail(), verificationCode);
        if (!isVerified) {
            model.addAttribute("error", "이메일 인증이 완료되지 않았습니다.");
            model.addAttribute("user", user);
            return "join";
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("user", user);
            return "join";
        }

        // 비밀번호 암호화 및 사용자 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        // 축하 이메일 전송
        try {
            verificationService.sendWelcomeEmail(user.getEmail());
        } catch (MessagingException e) {
            model.addAttribute("error", "회원가입은 완료되었으나 이메일 전송에 실패했습니다.");
            return "join";
        }

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, Principal principal) {
        if (principal == null) {
            // 비로그인 상태에서도 페이지는 반환하지만, 가입 폼 데이터는 전달하지 않음
            return "register";
        }

        // 로그인된 경우에만 가입 폼 데이터를 전달
        model.addAttribute("join", new Join());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("join") Join join, Principal principal, Model model) {
        if (principal == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "register";
        }

        // 로그인된 사용자 정보와 연동
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        join.setUser(user);
        join.setEmail(user.getEmail());
        joinRepository.save(join);

        model.addAttribute("success", "동아리 가입 신청이 완료되었습니다.");
        return "redirect:/";
    }
}
package net.hovering.Hovering_Web.login.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(nullable = false)
    private String name;

    // 학과
    @Column(nullable = false)
    private String department;

    // 학번
    @Column(unique = true, nullable = false)
    private String studentId;

    // 이메일 (로그인 ID)
    @Column(unique = true, nullable = false)
    private String email;

    // 전화번호
    @Column(nullable = false)
    private String phone;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 회원 등급 (0: Master, 1: 회장, 2: 임원, 3: 회원, 4: 일반)
    @Column(nullable = false)
    private int level;

    // 사용자 역할 (Master, 회장, 임원, 회원, 일반)
    @Column(nullable = false)
    private String role;

    // 최초 가입 신청 날짜
    @Column(nullable = false, updatable = false)
    private LocalDateTime firstRegisteredAt;

    // 최종 탈퇴 날짜
    @Column
    private LocalDateTime lastUnregisteredAt;

    // 재가입 신청 날짜
    @Column
    private LocalDateTime reRegisteredAt;

    // 최종 정보 변경 날짜
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    // 기본값 설정
    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = "일반"; // 역할
        }

        firstRegisteredAt = LocalDateTime.now(); // 최초 가입 신청 날짜
        lastUpdatedAt = LocalDateTime.now(); // 최종 정보 변경 날짜
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now(); // 최종 정보 변경 날짜
    }
}
package net.hovering.Hovering_Web.login.model;

import jakarta.persistence.*;
import lombok.*;
import net.hovering.Hovering_Web.join.model.Join;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(unique = true, nullable = false)
    private String studentId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String role;

    // 동아리 가입 신청 정보 - Join과의 관계 수정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Join> joins = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime firstRegisteredAt;

    @Column
    private LocalDateTime lastUnregisteredAt;

    @Column
    private LocalDateTime reRegisteredAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = "일반";
        }
        firstRegisteredAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
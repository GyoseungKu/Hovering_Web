package net.hovering.Hovering_Web.join.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "join_requests")
public class Join {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 동아리 지원 동기
    @Column(nullable = false, length = 1000)
    private String motivation;

    // 가장 흥미롭게 관찰한 새
    @Column(nullable = false, length = 500)
    private String favoriteBird;

    // 기타 하고 싶은 말
    @Column(length = 1000)
    private String additionalComment;

    // 신청 날짜
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}
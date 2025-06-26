package net.hovering.Hovering_Web.join.model;

import jakarta.persistence.*;
import lombok.*;
import net.hovering.Hovering_Web.login.model.User;

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

    @Column(nullable = false, length = 1000)
    private String motivation;

    @Column(nullable = false, length = 500)
    private String favoriteBird;

    @Column(length = 1000)
    private String additionalComment;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}
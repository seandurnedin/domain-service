package com.libraryapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "users")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "max_books_allowed", nullable = false)
    @Builder.Default
    private Integer maxBooksAllowed = 5;

    @Column(name = "loan_duration_days", nullable = false)
    @Builder.Default
    private Integer loanDurationDays = 14;

    @Column(name = "daily_late_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal dailyLateFee = new BigDecimal("0.50");

    @OneToMany(mappedBy = "userGroup")
    @Builder.Default
    private Set<User> users = new HashSet<>();
}

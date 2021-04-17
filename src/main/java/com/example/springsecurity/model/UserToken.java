package com.example.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"})})
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token")
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "last_use")
    private Long lastUse = System.currentTimeMillis();

    @Column(name = "create_at")
    private Long createAt;

    @Column(name = "valid_till")
    private Long validTill;

    public UserToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}

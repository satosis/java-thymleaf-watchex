package com.example.watchex.entity;

import com.example.watchex.common.TokenType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="token",length = 1000)
    private String token;

    @Column(name="token_exp_date")
    private Date tokenExpDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=true)
    private User user;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=true)
    private Admin admin;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}

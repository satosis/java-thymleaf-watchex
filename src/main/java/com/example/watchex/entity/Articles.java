package com.example.watchex.entity;

import com.example.watchex.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "articles")
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String a_name;

    private String a_slug;

    private Integer a_hot;
    private Integer a_active;
    private String a_menu_id;
    private Integer a_view;
    private String a_description;

    @Lob
    private Blob avatar;
    private String a_content;
    private Integer _wysihtml5_mode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}

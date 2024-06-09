package com.example.watchex.entity;

import com.example.watchex.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "keyword")
@Getter
@Setter
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String k_name;

    @Column(nullable = false)
    private String k_slug;

    @Column(nullable = false)
    private String k_description;

    @Column(nullable = false)
    private Integer k_hot;

    @Column(length = 45, nullable = false)
    private Integer k_active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public void setSlug() {
        this.k_slug = CommonUtils.toSlug(this.k_name);
    }
}

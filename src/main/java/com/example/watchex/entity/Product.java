package com.example.watchex.entity;

import com.example.watchex.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String pro_name;

    private String pro_slug;

    private Integer pro_amount;
    private Integer pro_view;
    private Integer pro_price;
    private Integer pro_sale;

    @ManyToOne
    @JoinColumn(name="pro_category", nullable=false)
    private Category pro_category;
    private Integer pro_avatar;
    private Integer pro_favourite;
    private Integer pro_hot;
    private Integer pro_active;
    private Integer pro_admin_id;
    private Integer pro_pay;
    private String pro_description;
    private String pro_content;
    private Integer pro_review_total;
    private String keywordseo;
    private Integer pro_review_star;
    private String _wysihtml5_mode;

    @ManyToMany
    Set<Keyword> keyword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public void setSlug() {
        this.pro_slug = CommonUtils.toSlug(this.pro_name);
    }
}

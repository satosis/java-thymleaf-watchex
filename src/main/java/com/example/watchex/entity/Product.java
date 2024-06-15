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
@Table(name = "products")
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
    private Category category;
    private String pro_avatar;
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany
    Set<Keyword> keyword;

    @OneToMany
    Set<Rating> ratings;
    public void setSlug() {
        this.pro_slug = CommonUtils.toSlug(this.pro_name);
    }

    public String getPro_avatar() {
        String[] partsCollArr;
        if (this.pro_avatar != null) {
            partsCollArr = this.pro_avatar.split("__");
            String dateAvatar = partsCollArr[0].replace('-', '/');
            return "http://localhost:8081/uploads/" + dateAvatar + "/" + this.pro_avatar;
        }
        return "http://localhost:8081/view/img/no-image.png";
    }
}

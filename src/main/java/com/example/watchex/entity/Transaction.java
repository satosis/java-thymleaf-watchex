package com.example.watchex.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tst_user_id", nullable = false)
    private User tst_user_id;
    private String tst_total_money;
    private String tst_name;
    private String tst_email;
    private String tst_phone;
    private String tst_address;
    private String tst_note;
    private Integer tst_status;
    private Integer tst_type;
    private String tst_code;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public String getStatus() {
        if (this.tst_status == 1 || this.tst_status == 2) {
            return "Đang vận chuyển";
        }
        if (this.tst_status == 3) {
            return "Đã giao hàng";
        }
        if (this.tst_status == -1) {
            return "Đã hủy";
        }
        if (this.tst_status == 4) {
            return "Hoàn thành";
        }
        if (this.tst_status == 5) {
            return "Chờ xác nhận";
        }
        return "Đã xác nhận";
    }

    public String getClassStatus() {
        if (this.tst_status == 1 || this.tst_status == 2) {
            return "btn btn-info";
        }
        if (this.tst_status == 3) {
            return "btn btn-success";
        }
        if (this.tst_status == -1) {
            return "btn btn-danger";
        }
        return "btn btn-primary";
    }
}

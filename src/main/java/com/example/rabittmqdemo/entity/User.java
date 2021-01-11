package com.example.rabittmqdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name="useres")
public class User implements Serializable {
    @Id
    @Column(name="id")
    private String id;
    /**
     * 用户名
     */
    @Column(name="user_name")
    private String userName;

    /**
     * 生日
     */
    @Column(name="birthday")
    private LocalDate birthday;

    /**
     * 地址
     */
    @Column(name="address")
    private String address;

    //添加默认构造器
    public User() {

    }

    public User(String id, String userName, LocalDate birthday, String address) {
        this.id = id;
        this.userName = userName;
        this.birthday = birthday;
        this.address = address;
    }
}

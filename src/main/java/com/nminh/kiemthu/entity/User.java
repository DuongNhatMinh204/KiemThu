package com.nminh.kiemthu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username ;

    @Column(nullable = false)
    private String password ;

    private Long teacherId ;

    private String roleName ;
}

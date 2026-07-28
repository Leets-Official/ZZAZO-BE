package org.example.zzazo.domain.department.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.zzazo.global.entity.BaseTimeEntity;

@Entity @Table(name = "department")
@NoArgsConstructor
@Getter
public class Department extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", nullable = false)
    private Long id;

    @Column(name = "department_name")
    private String name;


}

package org.example.zzazo.domain.department.repository;

import org.example.zzazo.domain.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
}

package com.ecommerce.project.repository;

import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.RolesName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findByRolesName(RolesName rolesName);
}

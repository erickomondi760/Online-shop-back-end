package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Roles{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RolesName rolesName;

    @ManyToMany(mappedBy = "userRoles")
    private List<User> users = new ArrayList<>();

    public Roles(RolesName rolesName) {
        this.rolesName = rolesName;
    }
}

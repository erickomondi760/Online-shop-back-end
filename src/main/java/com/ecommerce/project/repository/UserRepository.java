package com.ecommerce.project.repository;

import com.ecommerce.project.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    boolean existsByUsername(@NotBlank @Size(min = 3) String username);

    boolean existsByEmail(@Email String email);

    @Query("select u from User u join fetch u.addresses a where a.building = ?1")
    List<User> findByAddress(@Size(min = 4, message = "Building name must be at least 4 characters") @NotBlank String building);
}

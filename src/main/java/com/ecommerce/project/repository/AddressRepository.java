package com.ecommerce.project.repository;

import com.ecommerce.project.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByStreet(@Size(min = 2, message = "City name must be at least 2 characters") @NotBlank String street);

    @Query("select a from Address a join fetch a.users u where u.email = ?1")
    List<Address> findByUserEmail(String email);
}

package com.ecommerce.project.service;

import com.ecommerce.project.dto.AddressDTO;
import com.ecommerce.project.model.Address;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(Address address);

    List<AddressDTO> getAddresses();

    AddressDTO findAnAddress(Long id);

    List<AddressDTO> findByUserEmail(String userEmail);

    AddressDTO updateAnAddress(@Valid Address address, Long id);

    String deleteAnAddress(Long id);
}

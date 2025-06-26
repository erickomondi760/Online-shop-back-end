package com.ecommerce.project.controller;

import com.ecommerce.project.dto.AddressDTO;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.security.userdetails.AuthUtil;
import com.ecommerce.project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("addresses")
    public ResponseEntity<AddressDTO> createAnAddress(@Valid @RequestBody Address address){
        AddressDTO addressDTO = addressService.createAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressDTO);
    }

    @GetMapping("addresses")
    public ResponseEntity<List<AddressDTO>> getAnAddress(){
        List<AddressDTO> addressDTO = addressService.getAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @GetMapping("addresses/{id}")
    public ResponseEntity<AddressDTO> getAnAddress(@PathVariable Long id){
        AddressDTO addressDTO = addressService.findAnAddress(id);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @GetMapping("addresses/user")
    public ResponseEntity<List<AddressDTO>> findAnAddressByUser(){
        List<AddressDTO> addressDTO = addressService.findByUserEmail(authUtil.getloggedInEmail());
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @PutMapping("addresses/{id}")
    public ResponseEntity<AddressDTO> updateAnAddress(@Valid @RequestBody Address address
            ,@PathVariable Long id){
        AddressDTO addressDTO = addressService.updateAnAddress(address,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressDTO);
    }


    @DeleteMapping("addresses/{id}")
    public ResponseEntity<String> deleteAnAddress(@PathVariable Long id){
        String message = addressService.deleteAnAddress(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}

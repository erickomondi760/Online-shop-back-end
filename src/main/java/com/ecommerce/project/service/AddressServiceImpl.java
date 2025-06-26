package com.ecommerce.project.service;

import com.ecommerce.project.dto.AddressDTO;
import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.userdetails.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;


    @Transactional
    @Override
    public AddressDTO createAddress(Address address) {
//        Address addressFromDb = addressRepository.findByStreet(address.getStreet());
//
//        if(addressFromDb != null){
//            throw  new APIException("An address with a similar street exist");
//        }

        User loggedInUser = authUtil.getLoggedInUser();

        address.getUsers().add(loggedInUser);
        Address savedAddress = addressRepository.save(address);

        loggedInUser.getAddresses().add(savedAddress);
        userRepository.save(loggedInUser);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(address -> modelMapper
                .map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO findAnAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("" +
                "Address of id: "+id+" not found"));

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> findByUserEmail(String userEmail) {
        List<Address> address = addressRepository.findByUserEmail(userEmail);

        List<AddressDTO> addressDTOS = address.stream().map(address1 ->
                modelMapper.map(address1,AddressDTO.class)).toList();

        if(address.isEmpty()){
            throw new APIException("Address of the specified user does not exist");
        }
        return addressDTOS;
    }

    @Override
    public AddressDTO updateAnAddress(Address address, Long id) {
        Address fetchedAddress = addressRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("" +
                "Address of id: "+id+" not found"));

        fetchedAddress.setBuilding(address.getBuilding());
        fetchedAddress.setCity(address.getCity());
        fetchedAddress.setCountry(address.getCountry());
        fetchedAddress.setStreet(address.getStreet());
        fetchedAddress.setZipCode(address.getZipCode());

        Address updated = addressRepository.save(fetchedAddress);

        return modelMapper.map(updated,AddressDTO.class);
    }

    @Override
    public String deleteAnAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Specified address does not exist"));
        List<User> users = userRepository.findByAddress(address.getBuilding());
        for(User user:users){
            for(Address address1: user.getAddresses()){

            }
        }
        if(!users.isEmpty()){
        users.forEach(user -> {user.getAddresses().remove(address);
            userRepository.save(user);
            addressRepository.delete(address);
        });
            return "Address successfully deleted";
        }

        return "Failed to delete the specified address";

    }
}

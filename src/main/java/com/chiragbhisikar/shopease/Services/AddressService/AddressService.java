package com.chiragbhisikar.shopease.Services.AddressService;

import com.chiragbhisikar.shopease.Auth.Service.UserDetailService;
import com.chiragbhisikar.shopease.DTO.AddressDTO;
import com.chiragbhisikar.shopease.Model.Address;
import com.chiragbhisikar.shopease.Model.Auth.User;
import com.chiragbhisikar.shopease.Repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AddressService implements iAddressService {
    private final AddressRepository addressRepository;
    private final UserDetailService userDetailService;

    @Override
    public Address addAddress(AddressDTO reqAddress, Principal principal) {
        User user = (User) userDetailService.loadUserByUsername(principal.getName());
        Address Address = convertAddressDtoToEntity(reqAddress, user);
        return addressRepository.save(Address);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteAddress(UUID AddressId, Principal principal) {
        User user = (User) userDetailService.loadUserByUsername(principal.getName());
        addressRepository.findAddressByIdAndUserId(AddressId, user.getId())
                .orElseThrow(() -> new AuthorizationDeniedException("User Is Not Same Who Added Address !"));
        addressRepository.deleteById(AddressId);
    }

    @Override
    public Address convertAddressDtoToEntity(AddressDTO AddressDTO, User user) {

        Address address = Address.builder()
                .name(AddressDTO.getName())
                .street(AddressDTO.getStreet())
                .city(AddressDTO.getCity())
                .state(AddressDTO.getState())
                .zipCode(AddressDTO.getZipCode())
                .phoneNumber(AddressDTO.getPhoneNumber())
                .user(user)
                .build();

        return address;
    }
}

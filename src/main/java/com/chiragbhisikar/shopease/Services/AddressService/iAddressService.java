package com.chiragbhisikar.shopease.Services.AddressService;

import com.chiragbhisikar.shopease.DTO.AddressDTO;
import com.chiragbhisikar.shopease.Model.Address;
import com.chiragbhisikar.shopease.Model.Auth.User;

import java.security.Principal;
import java.util.UUID;

public interface iAddressService {

    public Address addAddress(AddressDTO reqAddress, Principal principal);

    public void deleteAddress(UUID AddressId, Principal principal);

    public Address convertAddressDtoToEntity(AddressDTO AddressDTO, User user);
}

package com.chiragbhisikar.shopease.Controller;

import com.chiragbhisikar.shopease.DTO.AddressDTO;
import com.chiragbhisikar.shopease.Model.Address;
import com.chiragbhisikar.shopease.Response.ApiResponse;
import com.chiragbhisikar.shopease.Services.AddressService.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> createAddress(@RequestBody AddressDTO addressRequest, Principal principal) {
        try {
            Address address = addressService.addAddress(addressRequest, principal);
            return new ResponseEntity<>(new ApiResponse("Address Created Successfully !", address), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable UUID id, Principal principal) {
        try {
            addressService.deleteAddress(id, principal);
            return new ResponseEntity<>(new ApiResponse("Address Deleted Successfully !", null), HttpStatus.OK);
        } catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Address Deleted Failed !", null), HttpStatus.BAD_REQUEST);
        }
    }
}